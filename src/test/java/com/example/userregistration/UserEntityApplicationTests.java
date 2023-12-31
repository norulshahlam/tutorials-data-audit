package com.example.userregistration;

import com.example.userregistration.entity.*;
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
import java.util.Random;

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
        bookingEntity.setCommodities(List.of(initCommodityData()));
        bookingEntity.setCustomers(List.of(initCustomerData()));
        bookingEntity.setEquipments(List.of(initEquipmentData()));
        bookingEntity.setDangerousGoods(List.of(initDangerousGoodsData()));
        bookingEntity.setVesselVoyages(List.of(initVesselVoyageData()));
        bookingEntity.setRefeerCargos(List.of(initRefeerCargoData()));

        contactEntity = initContactData();
    }

    Faker faker = new Faker();

    @RepeatedTest(10)
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
        restTemplate.postForObject(baseUrl.concat("/createBooking"), bookingEntity, BookingEntity.class);
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

    @RepeatedTest(10)
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
        id=id+1;
        String oldBkgNo = fetchedBooking.getBkgNo();
        String oldBkgRqstStatusSeq = fetchedBooking.getBlNo();

        fetchedBooking.setBkgNo(faker.idNumber().ssnValid());
        fetchedBooking.setBkgRqstStatusSeq(new Random().nextInt());

        BookingEntity editedBooking = restTemplate
                .postForObject(baseUrl.concat("/editBooking"), fetchedBooking, BookingEntity.class);

        String newBkgNo = editedBooking.getBkgNo();
        Integer newBkgRqstStatusSeq = editedBooking.getBkgRqstStatusSeq();

        log.info("oldBkgNo: " + oldBkgNo + " --> newBkgNo: " + newBkgNo);
        log.info("oldBkgRqstStatusSeq: " + oldBkgRqstStatusSeq + " --> newBkgRqstStatusSeq: " + newBkgRqstStatusSeq);

        assertThat(oldBkgNo, not(newBkgNo));
        assertThat(oldBkgRqstStatusSeq, not(newBkgRqstStatusSeq));

    }

    @RepeatedTest(10)
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
                .frtTermCode(faker.numerify("######"))
                .scacCode(faker.numerify("######"))
                .usaCstmsFileNo(faker.numerify("######"))
                .cndCstmsFileCode(faker.numerify("######"))
                .scNo(faker.numerify("######"))
                .rfaNo(faker.numerify("######"))
                .isRfa(String.valueOf(new Random().nextBoolean()))
                .status(String.valueOf(new Random().nextBoolean()))
                .build();
    }

    ContactEntity initContactData() {
        return ContactEntity.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .phoneNo(faker.phoneNumber().phoneNumber())
                .mobileNo(faker.phoneNumber().cellPhone())
                .status(faker.numerify("#####"))
                .build();
    }

    CommodityEntity initCommodityData() {
        return CommodityEntity.builder()
                .estWgt(faker.number().randomDouble(2, 1, 100))
                .estWgtUnitCode(faker.bothify("??####", true))
                .cmdtyCode(faker.bothify("??####", true))
                .cmdtyDesc(faker.bothify("??####", true))
                .socFlg(String.valueOf(new Random().nextBoolean()))
                .sltOwnrCode(faker.bothify("??####", true))
                .cntrOprCode(faker.bothify("??####", true))
                .isEmpty(faker.bothify("??####", true))
                .iasocFlg(String.valueOf(new Random().nextBoolean()))
                .iasocVoyCode(faker.bothify("??####", true))
                .iasocVslName(faker.bothify("??####", true))
                .status(faker.bothify("??####", true))
                .build();
    }

    CustomerEntity initCustomerData() {
        return CustomerEntity.builder()
                .bkgRqstSeq(new Random().nextInt())
                .custId(new Random().nextLong())
                .custTpCode(faker.bothify("??####", true))
                .cntCode(faker.bothify("??####", true))
                .custSeq(new Random().nextInt())
                .custName(faker.name().name())
                .custAddr(faker.address().fullAddress())
                .locCode(faker.bothify("??####", true))
                .locName(faker.address().fullAddress())
                .locCtnt(faker.number().digit())
                .pstCtnt(faker.number().digit())
                .custEmail(faker.internet().emailAddress())
                .cntcName(faker.name().name())
                .cntcEmail(faker.internet().emailAddress())
                .custErpRefNo(faker.bothify("??####", true))
                .eoriNo(faker.bothify("??####", true))
                .eurCstmsStName(faker.bothify("??####", true))
                .prnrCustCode(faker.bothify("??####", true))
                .deleteFlg(String.valueOf(new Random().nextBoolean()))
                .status(faker.bothify("??####", true))
                .build();
    }

    EquipmentsEntity initEquipmentData() {
        return EquipmentsEntity.builder()
                .eqpQtyId(new Random().nextLong())
                .cntrTpszCode(faker.numerify("######"))
                .cntrTpszName(faker.letterify("????????", true))
                .bkgRqstSeq(new Random().nextLong())
                .cntrQty(faker.number().randomDouble(2, 1, 100))
                .flexHgtFlg(String.valueOf(new Random().nextBoolean()))
                .deleteFlg(String.valueOf(new Random().nextBoolean()))
                .status(faker.bothify("####", true))
                .build();
    }

    DangerousGoodsEntity initDangerousGoodsData() {
        return DangerousGoodsEntity.builder()
                .dgGoId(new Random().nextLong())
                .dgGoSeq(new Random().nextInt())
                .bkgRqstSeq(new Random().nextInt())
                .cntrTpszCode(faker.bothify("??####", true))
                .cntrNo(faker.bothify("??####", true))
                .imdgUnNo(faker.bothify("??####", true))
                .imdgUnSeq(new Random().nextLong())
                .imdgClssId(faker.bothify("??####", true))
                .imdgPgNo(faker.bothify("??####", true))
                .pkgGrpCode1(faker.bothify("??####", true))
                .technicalName(faker.bothify("??####", true))
                .grsWgt(new Random().nextLong())
                .netWgt(new Random().nextLong())
                .prpShpName(faker.bothify("??####", true))
                .hzdCtnt(faker.bothify("??####", true))
                .flshPntCtnt(faker.bothify("??####", true))
                .flshPntTempUnitCode(faker.bothify("??####", true))
                .mrnPolutFlg(String.valueOf(new Random().nextBoolean()))
                .emerCntcPntCtnt(faker.bothify("??####", true))
                .dcgoStatusCode(faker.bothify("??####", true))
                .dgLmtQtyFlg(String.valueOf(new Random().nextBoolean()))
                .dcgoRmk(faker.bothify("??####", true))
                .dgCntrSeq(new Random().nextLong())
                .cntrCgoSeq(new Random().nextLong())
                .emerCntcPhnNoCtnt(faker.bothify("??####", true))
                .dgNetWgtUnitCode(faker.bothify("??####", true))
                .emerCntcEmail(faker.bothify("??####", true))
                .outrPkgCode(faker.bothify("??####", true))
                .outrPkgDesc(faker.bothify("??####", true))
                .outrPkgQty(new Random().nextLong())
                .inrPkgCode(faker.bothify("??####", true))
                .inrPkgDesc(faker.bothify("??####", true))
                .inrPkgQty(new Random().nextLong())
                .deleteFlg(String.valueOf(new Random().nextBoolean()))
                .status(faker.bothify("??####", true))
                .build();

    }

    VesselVoyageEntity initVesselVoyageData() {
        return VesselVoyageEntity.builder()
                .vslPrePstCode(faker.bothify("??####", true))
                .vslSeq(new Random().nextInt())
                .rqstMstSeq(new Random().nextInt())
                .localVoyNo(faker.bothify("??####", true))
                .polCode(faker.bothify("??####", true))
                .polName(faker.bothify("??####", true))
                .podCode(faker.bothify("??####", true))
                .routeTpCode(faker.bothify("??####", true))
                .etdDate(faker.date().birthday())
                .etaDate(faker.date().birthday())
                .vslCode(faker.bothify("??####", true))
                .vslName(faker.bothify("??####", true))
                .voyageCode(faker.bothify("??####", true))
                .deleteFlg(String.valueOf(new Random().nextBoolean()))
                .status(faker.bothify("??####", true))
                .build();
    }

    RefeerCargoEntity initRefeerCargoData() {
        return RefeerCargoEntity.builder()
                .rfCargoId(faker.bothify("??####", true))
                .rfCargoSeq(new Random().nextLong())
                .bkgRqstSeq(new Random().nextInt())
                .cntrTpszCode(faker.bothify("??####", true))
                .cntrNo(faker.bothify("??####", true))
                .minTemp(new Random().nextDouble())
                .minTempUnitCode(faker.bothify("??####", true))
                .maxTemp(new Random().nextDouble())
                .maxTempUnitCode(faker.bothify("??####", true))
                .humidRto(new Random().nextDouble())
                .cntrVentCode(faker.bothify("??####", true))
                .ventRto(new Random().nextDouble())
                .clngTpCode(faker.bothify("??####", true))
                .deleteFlg(String.valueOf(new Random().nextBoolean()))
                .cmdtyCode(faker.bothify("??####", true))
                .cmdtyDesc(faker.bothify("??####", true))
                .build();
    }

    BookingEntity initBookingData() {

        // initialize BookingEntity data using faker and builder class
        return BookingEntity.builder()
                .bkgRqstNo(faker.bothify("??-##-?##", true))
                .bkgRqstStatusSeq(new Random().nextInt())
                .bkgNo(faker.bothify("??-##-?##", true))
                .siNo(faker.bothify("??-##-?##", true))
                .bkgRqstsStatusCode(faker.bothify("??####", true))
                .bkgOfficeCode(faker.bothify("??####", true))
                .handleOfficeCode(faker.bothify("??####", true))
                .vslCode(faker.bothify("??####", true))
                .skdVoyNo(faker.bothify("??####", true))
                .skdDirCode(faker.bothify("??####", true))
                .vslName(faker.bothify("??####", true))
                .porCode(faker.bothify("??####", true))
                .porName(faker.bothify("??####", true))
                .polCode(faker.bothify("??####", true))
                .polName(faker.bothify("??####", true))
                .podCode(faker.bothify("??####", true))
                .podName(faker.bothify("??####", true))
                .delCode(faker.bothify("??####", true))
                .delName(faker.bothify("??####", true))
                .pkgQty(new Random().nextDouble())
                .pkgTypeCode(faker.bothify("??####", true))
                .measQty(new Random().nextLong())
                .measUnitCode(faker.bothify("??####", true))
                .netWgt(new Random().nextLong())
                .netWgtUnitCode(faker.bothify("??####", true))
                .netMeasQty(new Random().nextLong())
                .netMeasUnitCode(faker.bothify("??####", true))
                .dcFlg(String.valueOf(new Random().nextBoolean()))
                .rcFlg(String.valueOf(new Random().nextBoolean()))
                .awkCgoFlg(String.valueOf(new Random().nextBoolean()))
                .rqstDate(faker.date().birthday())
                .rqstDeltFlg(String.valueOf(new Random().nextBoolean()))
                .bkgUpldStatusCode(faker.bothify("??####", true))
                .rqstDepDate(faker.date().birthday())
                .rqstArrDate(faker.date().birthday())
                .bkgRmks1(faker.bothify("??####", true))
                .bkgRmks2(faker.bothify("??####", true))
                .autoCfmEdiFlg(String.valueOf(new Random().nextBoolean()))
                .autoEmailFlg(String.valueOf(new Random().nextBoolean()))
                .gdsDesc(faker.bothify("??####", true))
                .mkDesc(faker.bothify("??####", true))
                .rqstAcptUsrId(faker.bothify("??####", true))
                .rqstAcptUpdDate(faker.date().birthday())
                .obTrspModCode(faker.bothify("??####", true))
                .ibTrspModCode(faker.bothify("??####", true))
                .templateName(faker.bothify("??####", true))
                .deleteFlag(String.valueOf(new Random().nextBoolean()))
                .blNo(faker.bothify("??####", true))
                .siRqstStatusCode(faker.bothify("??####", true))
                .blRdyTpCode(faker.bothify("??####", true))
                .blCpyKnt(new Random().nextInt())
                .oblRdemKnt(new Random().nextInt())
                .blRdyOfficeCode(faker.bothify("??####", true))
                .blRdyUsrId(faker.bothify("??####", true))
                .blRdyDate(faker.date().birthday())
                .dgRmks(faker.bothify("??####", true))
                .actVoyNo(faker.bothify("??####", true))
                .docTpCode(faker.bothify("??####", true))
                .status(faker.bothify("??####", true))
                .transitTime(faker.bothify("??####", true))
                .voyageCode(faker.bothify("??####", true))
                .service(faker.bothify("??####", true))
                .gateInCutoffDate(faker.date().birthday())
                .vgmCutoffDate(faker.date().birthday())
                .documentationCutoffDate(faker.date().birthday())
                .build();
    }
}
