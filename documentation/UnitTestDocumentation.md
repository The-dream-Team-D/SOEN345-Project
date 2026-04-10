# Unit Test Documentation

This document covers all unit tests found under app/src/test/java.

Total unit tests documented: 103

Generated on: 2026-04-10

## AdminTest

Source file: app/src/test/java/com/example/popin/logicUnitTests/AdminTest.java

- addEvent_delegatesToEventCatalog
  Verifies: add Event delegates To Event Catalog.
- constructorAndUserAccessors_workCorrectly
  Verifies: constructor And User Accessors work Correctly.
- removeEvent_delegatesToEventCatalog
  Verifies: remove Event delegates To Event Catalog.
- updateEvent_delegatesToEventCatalog
  Verifies: update Event delegates To Event Catalog.

## AdminUserConversionTest

Source file: app/src/test/java/com/example/popin/logicUnitTests/AdminUserConversionTest.java

- adminConstructor_fromUserCopiesFieldsAndSetsAdminTrue
  Verifies: admin Constructor from User Copies Fields And Sets Admin True.

## BackButtonComponentViewTest

Source file: app/src/test/java/com/example/popin/activityUnitTests/BackButtonComponentViewTest.java

- backButtonGoesToMainActivity
  Verifies: back Button Goes To Main Activity.

## EventAdapterTest

Source file: app/src/test/java/com/example/popin/logicUnitTests/EventAdapterTest.java

- filter_byLocationAndDate_supportsTrimmedQuery
  Verifies: filter by Location And Date supports Trimmed Query.
- filter_byTitle_isCaseInsensitive
  Verifies: filter by Title is Case Insensitive.
- filter_emptyOrNull_restoresAllEvents
  Verifies: filter empty Or Null restores All Events.
- filter_noMatch_showsNoEvents
  Verifies: filter no Match shows No Events.
- initialState_showsAllEvents
  Verifies: initial State shows All Events.

## EventCatalogFlowTest

Source file: app/src/test/java/com/example/popin/logicUnitTests/EventCatalogFlowTest.java

- addEvent_failure_callsErrorCallback
  Verifies: add Event failure calls Error Callback.
- addEvent_success_callsSuccessCallback
  Verifies: add Event success calls Success Callback.
- deleteEventByName_missingEvent_returnsError
  Verifies: delete Event By Name missing Event returns Error.
- deleteEventByName_queryCancelled_returnsDatabaseError
  Verifies: delete Event By Name query Cancelled returns Database Error.
- updateEventByName_failure_returnsErrorCallback
  Verifies: update Event By Name failure returns Error Callback.
- updateEventByName_noFieldsProvided_returnsError
  Verifies: update Event By Name no Fields Provided returns Error.
- updateEventByName_success_callsSuccessCallback
  Verifies: update Event By Name success calls Success Callback.

## EventCatalogTest

Source file: app/src/test/java/com/example/popin/logicUnitTests/EventCatalogTest.java

- addEvent_emptyName_returnsErrorImmediately
  Verifies: add Event empty Name returns Error Immediately.
- addEvent_nullEvent_returnsErrorImmediately
  Verifies: add Event null Event returns Error Immediately.
- deleteEventByName_emptyName_returnsErrorImmediately
  Verifies: delete Event By Name empty Name returns Error Immediately.
- updateEventByName_emptyCurrentName_returnsErrorImmediately
  Verifies: update Event By Name empty Current Name returns Error Immediately.

## EventCatalogValidationExtraTest

Source file: app/src/test/java/com/example/popin/logicUnitTests/EventCatalogValidationExtraTest.java

- deleteEventByName_nullName_returnsErrorImmediately
  Verifies: delete Event By Name null Name returns Error Immediately.

## EventCategoryTest

Source file: app/src/test/java/com/example/popin/logicUnitTests/EventCategoryTest.java

- enum_containsExpectedValues
  Verifies: enum contains Expected Values.
- toStringOverrideWorks
  Verifies: to String Override Works.
- valueOf_returnsMatchingEnum
  Verifies: value Of returns Matching Enum.

## EventItemTest

Source file: app/src/test/java/com/example/popin/logicUnitTests/EventItemTest.java

- constructor_setsAllFields
  Verifies: constructor sets All Fields.
- convertTimeToLongFailOptions
  Verifies: convert Time To Long Fail Options.
- emptyConstructor_and_setters
  Verifies: empty Constructor and setters.
- setDateTimeFail
  Verifies: set Date Time Fail.

## LogicEnumsTest

Source file: app/src/test/java/com/example/popin/logicUnitTests/LogicEnumsTest.java

- eventFilterDateType_containsExpectedValues
  Verifies: event Filter Date Type contains Expected Values.
- notificationPreferenceOptions_containsExpectedValues
  Verifies: notification Preference Options contains Expected Values.
- notificationType_containsExpectedValues
  Verifies: notification Type contains Expected Values.
- userInputType_containsExpectedValues
  Verifies: user Input Type contains Expected Values.

## NavBarComponentViewTest

Source file: app/src/test/java/com/example/popin/activityUnitTests/NavBarComponentViewTest.java

- adminDashboardNotVisibleToNonAdminUser
  Verifies: admin Dashboard Not Visible To Non Admin User.
- adminDashboardVisibleToAdminUser
  Verifies: admin Dashboard Visible To Admin User.
- exploreTab_click_launchesEventsPageActivity
  Verifies: explore Tab click launches Events Page Activity.
- exploreTab_click_whenAlreadyOnEventsPage_doesNotLaunchNewActivity
  Verifies: explore Tab click when Already On Events Page does Not Launch New Activity.
- profileTab_click_launchesProfileActivity
  Verifies: profile Tab click launches Profile Activity.
- profileTab_click_whenAlreadyOnProfile_doesNotLaunchNewActivity
  Verifies: profile Tab click when Already On Profile does Not Launch New Activity.
- ticketsTab_click_launchesMyTicketsActivity
  Verifies: tickets Tab click launches My Tickets Activity.
- ticketsTab_click_whenAlreadyOnMyTickets_doesNotLaunchNewActivity
  Verifies: tickets Tab click when Already On My Tickets does Not Launch New Activity.

## NotificationDispatchTest

Source file: app/src/test/java/com/example/popin/logicUnitTests/NotificationDispatchTest.java

- IncorrectNotificationTypeResultsInError
  Verifies: Incorrect Notification Type Results In Error.
- sendNotification_changePassword_includesVerificationCode
  Verifies: send Notification change Password includes Verification Code.
- sendNotification_emailPreference_usesEmailServicer
  Verifies: send Notification email Preference uses Email Servicer.
- sendNotification_smsPreference_usesSmsServicer
  Verifies: send Notification sms Preference uses Sms Servicer.
- sendNotificationsDeleteAccountMessage
  Verifies: send Notifications Delete Account Message.
- sendNotificationsRegisterAccountMessage
  Verifies: send Notifications Register Account Message.

## NotificationsTest

Source file: app/src/test/java/com/example/popin/logicUnitTests/NotificationsTest.java

- buildCode_containsOnlyAlphaNumericCharacters
  Verifies: build Code contains Only Alpha Numeric Characters.
- buildCode_multipleCallsProduceNonConstantValues
  Verifies: build Code multiple Calls Produce Non Constant Values.
- buildCode_returnsSixCharacterString
  Verifies: build Code returns Six Character String.

## ProfileActivityTest

Source file: app/src/test/java/com/example/popin/activityUnitTests/ProfileActivityTest.java

- activityShouldLaunch
  Verifies: activity Should Launch.
- clickingEditShouldShowEditMode
  Verifies: clicking Edit Should Show Edit Mode.
- editFieldsShouldBePrefilled
  Verifies: edit Fields Should Be Prefilled.
- userDataShouldDisplayCorrectly
  Verifies: user Data Should Display Correctly.
- viewsShouldExist
  Verifies: views Should Exist.

## TicketAdapterTest

Source file: app/src/test/java/com/example/popin/logicUnitTests/TicketAdapterTest.java

- cancelButtonClick_callsCancelListener_forBoundTicket
  Verifies: cancel Button Click calls Cancel Listener for Bound Ticket.
- filter_nullOrEmpty_restoresAllTickets
  Verifies: filter null Or Empty restores All Tickets.
- initialState_showsAllTickets
  Verifies: initial State shows All Tickets.
- rowClick_callsTicketClickListener_forBoundTicket
  Verifies: row Click calls Ticket Click Listener for Bound Ticket.

## TicketItemTest

Source file: app/src/test/java/com/example/popin/logicUnitTests/TicketItemTest.java

- cancelTicket_failure_triggersOnError
  Verifies: cancel Ticket failure triggers On Error.
- cancelTicket_Success_triggersOnSuccess
  Verifies: cancel Ticket Success triggers On Success.
- constructor_setsTicketIdAndInheritedFields
  Verifies: constructor sets Ticket Id And Inherited Fields.
- emptyConstructor_startsWithNullTicketId
  Verifies: empty Constructor starts With Null Ticket Id.
- setTicketId_updatesValue
  Verifies: set Ticket Id updates Value.

## UserAccountFlowTest

Source file: app/src/test/java/com/example/popin/logicUnitTests/UserAccountFlowTest.java

- changePassword_failure_returnsErrorMessage
  Verifies: change Password failure returns Error Message.
- changePassword_success_callsSuccessCallback
  Verifies: change Password success calls Success Callback.
- changePassword_userMissing_returnsError
  Verifies: change Password user Missing returns Error.
- delete_failure_returnsErrorMessage
  Verifies: delete failure returns Error Message.
- delete_success_returnsSuccessMessage
  Verifies: delete success returns Success Message.
- delete_withBlankEmail_usesPhoneQueryBranch
  Verifies: delete with Blank Email uses Phone Query Branch.
- delete_withMissingUser_returnsError
  Verifies: delete with Missing User returns Error.
- forgotPassword_noUser_returnsError
  Verifies: forgot Password no User returns Error.
- forgotPassword_samePassword_returnsError
  Verifies: forgot Password same Password returns Error.
- forgotPassword_success_returnsUserWithPreference
  Verifies: forgot Password success returns User With Preference.
- signUp_duplicateUser_returnsError
  Verifies: sign Up duplicate User returns Error.
- signUp_emptyName_returnsValidationError
  Verifies: sign Up empty Name returns Validation Error.
- signUp_pushFailure_returnsError
  Verifies: sign Up push Failure returns Error.
- signUp_pushSuccess_callsSuccessAndNotification
  Verifies: sign Up push Success calls Success And Notification.
- signUp_queryCancelled_returnsDatabaseError
  Verifies: sign Up query Cancelled returns Database Error.
- updateProfile_queryCancelled_returnsError
  Verifies: update Profile query Cancelled returns Error.
- updateProfile_success_returnsUpdatedMessage
  Verifies: update Profile success returns Updated Message.
- updateProfile_userNotFound_returnsError
  Verifies: update Profile user Not Found returns Error.

## UserClassTests

Source file: app/src/test/java/com/example/popin/logicUnitTests/UserClassTests.java

- login_correctCredentials_callsOnSuccess
  Verifies: login correct Credentials calls On Success.
- login_databaseCancelled_callsOnError
  Verifies: login database Cancelled calls On Error.
- login_emailNotFound_callsOnError
  Verifies: login email Not Found calls On Error.
- login_emptyEmail_returnsError
  Verifies: login empty Email returns Error.
- login_emptyPassword_returnsError
  Verifies: login empty Password returns Error.
- login_nullEmail_returnsError
  Verifies: login null Email returns Error.
- login_nullPassword_returnsError
  Verifies: login null Password returns Error.
- login_wrongPassword_callsOnError
  Verifies: login wrong Password calls On Error.
- testEmptyConstructor
  Verifies: test Empty Constructor.
- testSettersAndGetters
  Verifies: test Setters And Getters.

## UserInSessionTest

Source file: app/src/test/java/com/example/popin/logicUnitTests/UserInSessionTest.java

- updateUser_withInstance_replacesCurrentUser
  Verifies: update User with Instance replaces Current User.
- updateUser_withoutInstance_doesNothing
  Verifies: update User without Instance does Nothing.
- userInSessionCanChange
  Verifies: user In Session Can Change.
- userInSessionCreateAffectsGetInstanceAndGetUser
  Verifies: user In Session Create Affects Get Instance And Get User.

## UserValidationAndFactoryTest

Source file: app/src/test/java/com/example/popin/logicUnitTests/UserValidationAndFactoryTest.java

- createUserWithEmail_setsEmailPreferenceAndTrimmedPassword
  Verifies: create User With Email sets Email Preference And Trimmed Password.
- createUserWithPhoneNumber_setsSmsPreferenceAndTrimmedPassword
  Verifies: create User With Phone Number sets Sms Preference And Trimmed Password.
- emailAndPhoneValidation_acceptsAndRejectsExpectedFormats
  Verifies: email And Phone Validation accepts And Rejects Expected Formats.
- identify_returnsExpectedInputType
  Verifies: identify returns Expected Input Type.
- setters_normalizeEmailPhoneAndPassword
  Verifies: setters normalize Email Phone And Password.
- setUserNotificationPreference_rejectsInvalidOrMissingContactInfo
  Verifies: set User Notification Preference rejects Invalid Or Missing Contact Info.

