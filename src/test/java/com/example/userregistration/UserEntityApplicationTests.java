package com.example.userregistration;

import com.example.userregistration.entity.BookingEntity;
import com.example.userregistration.entity.ContactEntity;
import com.example.userregistration.entity.ContractEntity;
import com.example.userregistration.repository.BookingRepository;
import com.example.userregistration.repository.ContactRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;


@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "mysql")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserEntityApplicationTests {

    @LocalServerPort
    private int port;

    private static RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ContactRepository contactRepository;
    private String baseUrl = "http://localhost:";
    BookingEntity bookingEntity = new BookingEntity();
    ContactEntity contactEntity = new ContactEntity();


    @BeforeAll
    static void beforeAll() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    void setUp() {
        baseUrl = baseUrl.concat(String.valueOf(port)).concat("/api/v1");
        bookingEntity = initBookingData();
        bookingEntity.setContacts(List.of(initContactData()));
        bookingEntity.setContracts(List.of(initContractData()));
        contactEntity = initContactData();
    }

    Faker faker = new Faker();

    @RepeatedTest(1)
    @Order(1)
    @DisplayName("Create Booking using java object")
    void createBooking() throws JsonProcessingException {
        /* Copy this log data to create json file */
        log.info("bookingEntity: " + objectMapper.writeValueAsString(bookingEntity));
        restTemplate.postForObject(baseUrl.concat("/createBooking"), bookingEntity, BookingEntity.class);
        List<BookingEntity> all = bookingRepository.findAll();
        assertThat(all, hasSize(greaterThanOrEqualTo(1)));
    }

    @RepeatedTest(1)
    @Order(2)
    @DisplayName("Create booking using json")
    void createBookingUsingJson() throws Exception {
        File file = new File("src/test/resources/bookingDto.json");
        bookingEntity = objectMapper.readValue(file, BookingEntity.class);
        BookingEntity bookingSaved = restTemplate.postForObject(baseUrl.concat("/createBooking"), bookingEntity, BookingEntity.class);
        log.info("booking saved: \n" + objectMapper.writeValueAsString(bookingSaved));
        List<BookingEntity> all = bookingRepository.findAll();
        assertThat(all, hasSize(greaterThanOrEqualTo(1)));
    }

    @RepeatedTest(10)
    @Order(3)
    @DisplayName("Create Contact using java object")
    void createContact() throws JsonProcessingException {
        /* Copy this log data to create json file */
        log.info("ContactEntity: " + objectMapper.writeValueAsString(contactEntity));
        restTemplate.postForObject(baseUrl.concat("/createContact"), contactEntity, ContactEntity.class);
        List<ContactEntity> all = contactRepository.findAll();
        assertThat(all, hasSize(greaterThanOrEqualTo(1)));
    }

    @RepeatedTest(1)
    @Order(5)
    @DisplayName("Edit booking using java object")
    void editBooking() {

        /* Fetch booking first */
        long id = 1L;
        String getUri = baseUrl.concat("/fetchBooking/").concat(Long.toString(id));
        log.info("getUri: " + getUri);
        BookingEntity fetchedBooking = restTemplate
                .getForEntity(getUri, BookingEntity.class).getBody();

        /* With fetched booking, edit some fields */
        id = id + 1;
        String oldBkgNo = fetchedBooking.getBkgNo();

        fetchedBooking.setBkgNo(faker.idNumber().ssnValid());
        fetchedBooking.setBkgRqstStatusSeq(Integer.valueOf(faker.bothify("###")));

        BookingEntity editedBooking = restTemplate
                .postForObject(baseUrl.concat("/editBooking"), fetchedBooking, BookingEntity.class);

        String newBkgNo = editedBooking.getBkgNo();
        log.info("oldBkgNo: " + oldBkgNo + " --> newBkgNo: " + newBkgNo);

        assertThat(oldBkgNo, not(newBkgNo));
    }

    @RepeatedTest(1)
    @Order(6)
    @DisplayName("Edit Contact using java object")
    void editContact() throws JsonProcessingException {
        List<ContactEntity> contactEntities = contactRepository.findAll();
        ContactEntity contactEntity = contactEntities.get(0);
        log.info("bookingEntity: " + objectMapper.writeValueAsString(contactEntities));
        String oldName = contactEntity.getName();
        String oldMobileNo = contactEntity.getMobileNo();

        contactEntity.setName(faker.name().fullName());
        contactEntity.setMobileNo(faker.phoneNumber().cellPhone());

        ContactEntity editedContact = restTemplate.postForObject(baseUrl.concat("/editContact"), contactEntity, ContactEntity.class);

        String newName = editedContact.getName();
        String newMobileNo = editedContact.getMobileNo();

        log.info("oldName: " + oldName + " --> newName: " + newName);
        log.info("oldMobileNo: " + oldMobileNo + " --> newMobileNo: " + newMobileNo);

        assertThat(oldName, not(newName));
        assertThat(oldMobileNo, not(newMobileNo));
    }

    @RepeatedTest(10)
    @Order(7)
    @DisplayName("Delete Booking by id")
    void deleteBooking() {
        List<BookingEntity> all = bookingRepository.findAll();
        bookingEntity = all.get(0);
        Long id = bookingEntity.getId();
        log.info("id: " + id);
        restTemplate.delete(baseUrl.concat("/deleteBooking/").concat(id.toString()));
        Optional<BookingEntity> byId = bookingRepository.findById(id);
        assertThat(byId, is(Optional.empty()));
    }

    @RepeatedTest(10)
    @Order(8)
    @DisplayName("Delete Contact by id")
    void deleteContact() {
        List<ContactEntity> all = contactRepository.findAll();
        ContactEntity contactEntity = all.get(0);
        Long id = contactEntity.getId();
        log.info("id: " + id);
        restTemplate.delete(baseUrl.concat("/deleteContact/").concat(id.toString()));
        Optional<ContactEntity> byId = contactRepository.findById(id);
        assertThat(byId, is(Optional.empty()));
    }

    ContractEntity initContractData() {
        return ContractEntity.builder()
                .frtTermCode(faker.numerify("####"))
                .status("T")
                .build();
    }

    ContactEntity initContactData() {
        return ContactEntity.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .mobileNo(faker.phoneNumber().cellPhone())
                .build();
    }

    BookingEntity initBookingData() {

        // initialize BookingEntity data using faker and builder class
        return BookingEntity.builder()
                .bkgRqstNo(faker.bothify("??-####", true))
                .bkgRqstStatusSeq(Integer.valueOf(faker.bothify("###", true)))
                .bkgNo(faker.bothify("??##", true))
                .build();
    }
}
