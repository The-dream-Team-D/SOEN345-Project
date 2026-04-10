# Unit Test Documentation

## Scope

- Source: app/src/test/java
- Total unit tests: 103
- Last updated: 2026-04-10

## Documentation Format

- Tests are numbered within each class for easier scanning.
- Intent: Behavior verified by the test.
- Expected checks: Assertions/verifications extracted from test code.

## AdminTest

Source file: app\src\test\java\com\example\popin\logicUnitTests\AdminTest.java

### Test 1: addEvent_delegatesToEventCatalog
Intent: add Event delegates To Event Catalog.
Expected checks:
- verify(mockCatalog, times(1)).addEvent(any(EventItem.class), eq(callback));

### Test 2: constructorAndUserAccessors_workCorrectly
Intent: constructor And User Accessors work Correctly.
Expected checks:
- assertNotNull(admin);
- assertEquals("admin@example.com", admin.getEmail());
- assertEquals("secret", admin.getPassword());

### Test 3: removeEvent_delegatesToEventCatalog
Intent: remove Event delegates To Event Catalog.
Expected checks:
- verify(mockCatalog).deleteEventByName("SOEN Mixer", callback);

### Test 4: updateEvent_delegatesToEventCatalog
Intent: update Event delegates To Event Catalog.
Expected checks:
- verify(mockCatalog).updateEventByName(

## AdminUserConversionTest

Source file: app\src\test\java\com\example\popin\logicUnitTests\AdminUserConversionTest.java

### Test 1: adminConstructor_fromUserCopiesFieldsAndSetsAdminTrue
Intent: admin Constructor from User Copies Fields And Sets Admin True.
Expected checks:
- assertEquals("Kevin", admin.getName());
- assertEquals("user@example.com", admin.getEmail());
- assertEquals("+15145551234", admin.getPhoneNumber());
- assertEquals("123 Main", admin.getAddress());
- assertEquals("about", admin.getBio());
- assertEquals(NotificationPreferenceOptions.EMAIL, admin.getUserNotificationPreference());
- assertTrue(admin.getIsAdmin());

## BackButtonComponentViewTest

Source file: app\src\test\java\com\example\popin\activityUnitTests\BackButtonComponentViewTest.java

### Test 1: backButtonGoesToMainActivity
Intent: back Button Goes To Main Activity.
Expected checks:
- assertNotNull("Expected an Intent to be started", started);
- assertEquals(MainActivity.class.getName(),

## EventAdapterTest

Source file: app\src\test\java\com\example\popin\logicUnitTests\EventAdapterTest.java

### Test 1: filter_byLocationAndDate_supportsTrimmedQuery
Intent: filter by Location And Date supports Trimmed Query.
Expected checks:
- assertEquals(1, adapter.getItemCount());
- assertEquals(1, adapter.getItemCount());

### Test 2: filter_byTitle_isCaseInsensitive
Intent: filter by Title is Case Insensitive.
Expected checks:
- assertEquals(1, adapter.getItemCount());
- assertEquals(1, adapter.getItemCount());

### Test 3: filter_emptyOrNull_restoresAllEvents
Intent: filter empty Or Null restores All Events.
Expected checks:
- assertEquals(1, adapter.getItemCount());
- assertEquals(5, adapter.getItemCount());
- assertEquals(5, adapter.getItemCount());

### Test 4: filter_noMatch_showsNoEvents
Intent: filter no Match shows No Events.
Expected checks:
- assertEquals(0, adapter.getItemCount());

### Test 5: initialState_showsAllEvents
Intent: initial State shows All Events.
Expected checks:
- assertEquals(5, adapter.getItemCount());

## EventCatalogFlowTest

Source file: app\src\test\java\com\example\popin\logicUnitTests\EventCatalogFlowTest.java

### Test 1: addEvent_failure_callsErrorCallback
Intent: add Event failure calls Error Callback.
Expected checks:
- fail("Expected failure");
- assertEquals("Failed to add event: write-failed", error.get());

### Test 2: addEvent_success_callsSuccessCallback
Intent: add Event success calls Success Callback.
Expected checks:
- fail("Expected success");
- assertEquals("Event added successfully", success.get());

### Test 3: deleteEventByName_missingEvent_returnsError
Intent: delete Event By Name missing Event returns Error.
Expected checks:
- fail("Expected missing-event error");
- assertEquals("No event found with that name", error.get());

### Test 4: deleteEventByName_queryCancelled_returnsDatabaseError
Intent: delete Event By Name query Cancelled returns Database Error.
Expected checks:
- fail("Expected cancellation error");
- assertEquals("Database error: cancelled", error.get());

### Test 5: updateEventByName_failure_returnsErrorCallback
Intent: update Event By Name failure returns Error Callback.
Expected checks:
- fail("Expected update failure");
- assertEquals("Failed to update event: update-failed", error.get());

### Test 6: updateEventByName_noFieldsProvided_returnsError
Intent: update Event By Name no Fields Provided returns Error.
Expected checks:
- fail("Expected no-fields error");
- assertEquals("No fields provided to update", error.get());

### Test 7: updateEventByName_success_callsSuccessCallback
Intent: update Event By Name success calls Success Callback.
Expected checks:
- fail("Expected update success");
- assertEquals("Event updated successfully", success.get());
- verify(eventRef).updateChildren(any());

## EventCatalogTest

Source file: app\src\test\java\com\example\popin\logicUnitTests\EventCatalogTest.java

### Test 1: addEvent_emptyName_returnsErrorImmediately
Intent: add Event empty Name returns Error Immediately.
Expected checks:
- fail("Expected error");
- assertEquals("Event name is empty", error.get());

### Test 2: addEvent_nullEvent_returnsErrorImmediately
Intent: add Event null Event returns Error Immediately.
Expected checks:
- fail("Expected error");
- assertEquals("Event is null", error.get());

### Test 3: deleteEventByName_emptyName_returnsErrorImmediately
Intent: delete Event By Name empty Name returns Error Immediately.
Expected checks:
- fail("Expected error");
- assertEquals("Event name is empty", error.get());

### Test 4: updateEventByName_emptyCurrentName_returnsErrorImmediately
Intent: update Event By Name empty Current Name returns Error Immediately.
Expected checks:
- fail("Expected error");
- assertEquals("Event name is empty", error.get());

## EventCatalogValidationExtraTest

Source file: app\src\test\java\com\example\popin\logicUnitTests\EventCatalogValidationExtraTest.java

### Test 1: deleteEventByName_nullName_returnsErrorImmediately
Intent: delete Event By Name null Name returns Error Immediately.
Expected checks:
- fail("Expected error");
- assertEquals("Event name is empty", error.get());

## EventCategoryTest

Source file: app\src\test\java\com\example\popin\logicUnitTests\EventCategoryTest.java

### Test 1: enum_containsExpectedValues
Intent: enum contains Expected Values.
Expected checks:
- assertEquals(5, EventCategory.values().length);
- assertTrue(Arrays.asList(EventCategory.values()).contains(EventCategory.SOCIAL));
- assertTrue(Arrays.asList(EventCategory.values()).contains(EventCategory.EDUCATIONAL));
- assertTrue(Arrays.asList(EventCategory.values()).contains(EventCategory.PROFESSIONAL));
- assertTrue(Arrays.asList(EventCategory.values()).contains(EventCategory.SPORTS));
- assertTrue(Arrays.asList(EventCategory.values()).contains(EventCategory.ENTERTAINMENT));

### Test 2: toStringOverrideWorks
Intent: to String Override Works.
Expected checks:
- assertEquals("Entertainment", EventCategory.ENTERTAINMENT.toString());

### Test 3: valueOf_returnsMatchingEnum
Intent: value Of returns Matching Enum.
Expected checks:
- assertEquals(EventCategory.SOCIAL, EventCategory.valueOf("SOCIAL"));
- assertEquals(EventCategory.ENTERTAINMENT, EventCategory.valueOf("ENTERTAINMENT"));

## EventItemTest

Source file: app\src\test\java\com\example\popin\logicUnitTests\EventItemTest.java

### Test 1: constructor_setsAllFields
Intent: constructor sets All Fields.
Expected checks:
- assertEquals("SOEN Mixer", item.getTitle());
- assertEquals(EventItem.convertTimeToLong(2026, 2, 20, 18, 0), item.getDateTime());
- assertEquals("EV Building Lobby", item.getLocation());
- assertEquals("Meet other SOEN students, network, and enjoy snacks in a casual social setting.", item.getDetails());

### Test 2: convertTimeToLongFailOptions
Intent: convert Time To Long Fail Options.
Expected checks:
- assertEquals(-1L, result);
- assertEquals(-1L, result);
- assertEquals(-1L, result);
- assertEquals(-1L, result);
- assertEquals(-1, result);

### Test 3: emptyConstructor_and_setters
Intent: empty Constructor and setters.
Expected checks:
- assertNull(item.getTitle());
- assertEquals("1234", item.getEventID());
- assertEquals("New Title", item.getTitle());
- assertEquals(EventItem.convertTimeToLong(2024, 0, 1, 12, 0), item.getDateTime());
- assertEquals("Montreal", item.getLocation());
- assertEquals("Sample event description", item.getDetails());
- assertEquals(20, item.getCapacity());
- assertEquals(15, item.getAttendeeCount());
- assertEquals(imgUrl, item.getImgURL());
- assertEquals(EventCategory.ENTERTAINMENT, item.getCategory());
- assertEquals(time, item.getDateTime());

### Test 4: setDateTimeFail
Intent: set Date Time Fail.
Expected checks:
- assertNull(item.getTitle());
- assertEquals(0, item.getDateTime());
- assertFalse(result);

## LogicEnumsTest

Source file: app\src\test\java\com\example\popin\logicUnitTests\LogicEnumsTest.java

### Test 1: eventFilterDateType_containsExpectedValues
Intent: event Filter Date Type contains Expected Values.
Expected checks:
- assertEquals(3, EventFilterDateType.values().length);
- assertTrue(Arrays.asList(EventFilterDateType.values()).contains(EventFilterDateType.PAST));
- assertTrue(Arrays.asList(EventFilterDateType.values()).contains(EventFilterDateType.UPCOMING));
- assertTrue(Arrays.asList(EventFilterDateType.values()).contains(EventFilterDateType.ALL));

### Test 2: notificationPreferenceOptions_containsExpectedValues
Intent: notification Preference Options contains Expected Values.
Expected checks:
- assertEquals(2, NotificationPreferenceOptions.values().length);
- assertTrue(Arrays.asList(NotificationPreferenceOptions.values()).contains(NotificationPreferenceOptions.EMAIL));
- assertTrue(Arrays.asList(NotificationPreferenceOptions.values()).contains(NotificationPreferenceOptions.SMS));

### Test 3: notificationType_containsExpectedValues
Intent: notification Type contains Expected Values.
Expected checks:
- assertEquals(5, NotificationType.values().length);
- assertTrue(Arrays.asList(NotificationType.values()).contains(NotificationType.REGISTER_EVENT));
- assertTrue(Arrays.asList(NotificationType.values()).contains(NotificationType.REGISTER_ACCOUNT));
- assertTrue(Arrays.asList(NotificationType.values()).contains(NotificationType.DELETE_ACCOUNT));
- assertTrue(Arrays.asList(NotificationType.values()).contains(NotificationType.CANCEL_TICKET));
- assertTrue(Arrays.asList(NotificationType.values()).contains(NotificationType.CHANGE_PASSWORD));

### Test 4: userInputType_containsExpectedValues
Intent: user Input Type contains Expected Values.
Expected checks:
- assertEquals(3, UserInputType.values().length);
- assertTrue(Arrays.asList(UserInputType.values()).contains(UserInputType.EMAIL));
- assertTrue(Arrays.asList(UserInputType.values()).contains(UserInputType.PHONE));
- assertTrue(Arrays.asList(UserInputType.values()).contains(UserInputType.UNKNOWN));

## NavBarComponentViewTest

Source file: app\src\test\java\com\example\popin\activityUnitTests\NavBarComponentViewTest.java

### Test 1: adminDashboardNotVisibleToNonAdminUser
Intent: admin Dashboard Not Visible To Non Admin User.
Expected checks:
- assertEquals(View.GONE, adminNavBarOption.getVisibility());

### Test 2: adminDashboardVisibleToAdminUser
Intent: admin Dashboard Visible To Admin User.
Expected checks:
- assertNotNull(started);
- assertEquals(AdminDashboardActivity.class.getName(),

### Test 3: exploreTab_click_launchesEventsPageActivity
Intent: explore Tab click launches Events Page Activity.
Expected checks:
- assertNotNull("Expected an Intent to be started", started);
- assertEquals(EventsPageActivity.class.getName(),

### Test 4: exploreTab_click_whenAlreadyOnEventsPage_doesNotLaunchNewActivity
Intent: explore Tab click when Already On Events Page does Not Launch New Activity.
Expected checks:
- assertNull("Should not start a new Activity when already on EventsPageActivity",

### Test 5: profileTab_click_launchesProfileActivity
Intent: profile Tab click launches Profile Activity.
Expected checks:
- assertNotNull(started);
- assertEquals(ProfileActivity.class.getName(),

### Test 6: profileTab_click_whenAlreadyOnProfile_doesNotLaunchNewActivity
Intent: profile Tab click when Already On Profile does Not Launch New Activity.
Expected checks:
- assertNull(shadowOf(profileActivity).getNextStartedActivity());

### Test 7: ticketsTab_click_launchesMyTicketsActivity
Intent: tickets Tab click launches My Tickets Activity.
Expected checks:
- assertNotNull(started);
- assertEquals(MyTicketsActivity.class.getName(),

### Test 8: ticketsTab_click_whenAlreadyOnMyTickets_doesNotLaunchNewActivity
Intent: tickets Tab click when Already On My Tickets does Not Launch New Activity.
Expected checks:
- assertNull(shadowOf(ticketsActivity).getNextStartedActivity());

## NotificationDispatchTest

Source file: app\src\test\java\com\example\popin\logicUnitTests\NotificationDispatchTest.java

### Test 1: IncorrectNotificationTypeResultsInError
Intent: Incorrect Notification Type Results In Error.
Expected checks:
- assertThrows(ArrayIndexOutOfBoundsException.class, () -> sendNotification(user, "", invalid, ""));

### Test 2: sendNotification_changePassword_includesVerificationCode
Intent: send Notification change Password includes Verification Code.
Expected checks:
- mockedEmail.verify(() ->

### Test 3: sendNotification_emailPreference_usesEmailServicer
Intent: send Notification email Preference uses Email Servicer.
Expected checks:
- mockedEmail.verify(() ->

### Test 4: sendNotification_smsPreference_usesSmsServicer
Intent: send Notification sms Preference uses Sms Servicer.
Expected checks:
- mockedSms.verify(() ->

### Test 5: sendNotificationsDeleteAccountMessage
Intent: send Notifications Delete Account Message.
Expected checks:
- mockedEmail.verify(() ->

### Test 6: sendNotificationsRegisterAccountMessage
Intent: send Notifications Register Account Message.
Expected checks:
- mockedEmail.verify(() ->

## NotificationsTest

Source file: app\src\test\java\com\example\popin\logicUnitTests\NotificationsTest.java

### Test 1: buildCode_containsOnlyAlphaNumericCharacters
Intent: build Code contains Only Alpha Numeric Characters.
Expected checks:
- assertTrue(Character.isLetterOrDigit(c));

### Test 2: buildCode_multipleCallsProduceNonConstantValues
Intent: build Code multiple Calls Produce Non Constant Values.
Expected checks:
- assertTrue(generatedCodes.size() > 1);

### Test 3: buildCode_returnsSixCharacterString
Intent: build Code returns Six Character String.
Expected checks:
- assertEquals(6, code.length());

## ProfileActivityTest

Source file: app\src\test\java\com\example\popin\activityUnitTests\ProfileActivityTest.java

### Test 1: activityShouldLaunch
Intent: activity Should Launch.
Expected checks:
- assertNotNull(activity);

### Test 2: clickingEditShouldShowEditMode
Intent: clicking Edit Should Show Edit Mode.
Expected checks:
- assertEquals(EditText.VISIBLE, etName.getVisibility());
- assertEquals(EditText.VISIBLE, etAddress.getVisibility());
- assertEquals(EditText.VISIBLE, etPhone.getVisibility());
- assertEquals(EditText.VISIBLE, etBio.getVisibility());
- assertEquals(Button.VISIBLE, btnSave.getVisibility());
- assertEquals(Button.GONE, btnEdit.getVisibility());

### Test 3: editFieldsShouldBePrefilled
Intent: edit Fields Should Be Prefilled.
Expected checks:
- assertEquals("HOSSAM THE GOAT", etName.getText().toString());
- assertEquals("123 concordia", etAddress.getText().toString());
- assertEquals("123456789", etPhone.getText().toString());
- assertEquals("bio 123", etBio.getText().toString());

### Test 4: userDataShouldDisplayCorrectly
Intent: user Data Should Display Correctly.
Expected checks:
- assertEquals("Name: HOSSAM THE GOAT", tvName.getText().toString());
- assertEquals("Email: hos@email.com", tvEmail.getText().toString());
- assertEquals("Address: 123 concordia", tvAddress.getText().toString());
- assertEquals("Phone: 123456789", tvPhone.getText().toString());
- assertEquals("Bio: bio 123", tvBio.getText().toString());
- assertEquals("Notification Preference:Email", tvNotifPref.getText().toString());

### Test 5: viewsShouldExist
Intent: views Should Exist.
Expected checks:
- assertNotNull(tvName);
- assertNotNull(tvEmail);
- assertNotNull(tvAddress);
- assertNotNull(tvPhone);
- assertNotNull(tvBio);
- assertNotNull(tvNotifPref);
- assertNotNull(etName);
- assertNotNull(etAddress);
- assertNotNull(etPhone);
- assertNotNull(etBio);
- assertNotNull(btnEdit);
- assertNotNull(btnSave);

## TicketAdapterTest

Source file: app\src\test\java\com\example\popin\logicUnitTests\TicketAdapterTest.java

### Test 1: cancelButtonClick_callsCancelListener_forBoundTicket
Intent: cancel Button Click calls Cancel Listener for Bound Ticket.
Expected checks:
- assertNotNull(cancelled.get());
- assertEquals("t2", cancelled.get().getTicketId());
- assertEquals("Hackathon Kickoff", cancelled.get().getTitle());

### Test 2: filter_nullOrEmpty_restoresAllTickets
Intent: filter null Or Empty restores All Tickets.
Expected checks:
- assertEquals(1, adapter.getItemCount());
- assertEquals(3, adapter.getItemCount());
- assertEquals(3, adapter.getItemCount());

### Test 3: initialState_showsAllTickets
Intent: initial State shows All Tickets.
Expected checks:
- assertEquals(3, adapter.getItemCount());

### Test 4: rowClick_callsTicketClickListener_forBoundTicket
Intent: row Click calls Ticket Click Listener for Bound Ticket.
Expected checks:
- assertNotNull(opened.get());
- assertEquals("t3", opened.get().getTicketId());
- assertEquals("AI Study Jam", opened.get().getTitle());

## TicketItemTest

Source file: app\src\test\java\com\example\popin\logicUnitTests\TicketItemTest.java

### Test 1: cancelTicket_failure_triggersOnError
Intent: cancel Ticket failure triggers On Error.
Expected checks:
- fail("The test should not result in On Success");
- assertEquals(2, success[0]);
- assertEquals("Cancellation failed.", errorMessage[0]);

### Test 2: cancelTicket_Success_triggersOnSuccess
Intent: cancel Ticket Success triggers On Success.
Expected checks:
- fail("The test should not result in On Error: " + message);
- assertEquals(1, success[0]);
- assertEquals("Ticket cancelled successfully", errorMessage[0]);

### Test 3: constructor_setsTicketIdAndInheritedFields
Intent: constructor sets Ticket Id And Inherited Fields.
Expected checks:
- assertEquals("ticket-1", item.getTicketId());
- assertEquals("SOEN Mixer", item.getTitle());
- assertEquals(when, item.getDateTime());
- assertEquals("EV Building Lobby", item.getLocation());

### Test 4: emptyConstructor_startsWithNullTicketId
Intent: empty Constructor starts With Null Ticket Id.
Expected checks:
- assertNull(item.getTicketId());

### Test 5: setTicketId_updatesValue
Intent: set Ticket Id updates Value.
Expected checks:
- assertEquals("ticket-99", item.getTicketId());

## UserAccountFlowTest

Source file: app\src\test\java\com\example\popin\logicUnitTests\UserAccountFlowTest.java

### Test 1: changePassword_failure_returnsErrorMessage
Intent: change Password failure returns Error Message.
Expected checks:
- fail("Expected failure callback");
- assertEquals("Failed to update password: update-failed", error.get());

### Test 2: changePassword_success_callsSuccessCallback
Intent: change Password success calls Success Callback.
Expected checks:
- fail("Expected success");
- assertEquals("Password Changed Successfully!", success.get());

### Test 3: changePassword_userMissing_returnsError
Intent: change Password user Missing returns Error.
Expected checks:
- fail("Expected missing-user error");
- assertEquals("No user with that email/phone number", error.get());

### Test 4: delete_failure_returnsErrorMessage
Intent: delete failure returns Error Message.
Expected checks:
- fail("Expected delete failure");
- assertEquals("Failed to Delete DB Account", error.get());

### Test 5: delete_success_returnsSuccessMessage
Intent: delete success returns Success Message.
Expected checks:
- fail("Expected delete success");
- assertEquals("Deleted Account Successfully!", success.get());

### Test 6: delete_withBlankEmail_usesPhoneQueryBranch
Intent: delete with Blank Email uses Phone Query Branch.
Expected checks:
- public void onSuccess(String message) { fail("Expected error path"); }
- assertEquals("Error encountered locating user in DB", message);

### Test 7: delete_withMissingUser_returnsError
Intent: delete with Missing User returns Error.
Expected checks:
- public void onSuccess(String message) { fail("Expected delete error"); }
- assertEquals("Error encountered locating user in DB", error.get());

### Test 8: forgotPassword_noUser_returnsError
Intent: forgot Password no User returns Error.
Expected checks:
- fail("Expected no-user error");
- assertEquals("No user with that email/phone number", error.get());

### Test 9: forgotPassword_samePassword_returnsError
Intent: forgot Password same Password returns Error.
Expected checks:
- fail("Expected error for unchanged password");
- assertEquals("New password can't be the same as old password", error.get());

### Test 10: forgotPassword_success_returnsUserWithPreference
Intent: forgot Password success returns User With Preference.
Expected checks:
- assertEquals("+15145551234", user.getPhoneNumber());
- assertEquals(NotificationPreferenceOptions.SMS, user.getUserNotificationPreference());
- fail("Expected forgotPassword success");

### Test 11: signUp_duplicateUser_returnsError
Intent: sign Up duplicate User returns Error.
Expected checks:
- fail("Expected duplicate user error");
- assertEquals("An account with this Email/Phone Number already exists", error.get());

### Test 12: signUp_emptyName_returnsValidationError
Intent: sign Up empty Name returns Validation Error.
Expected checks:
- fail("Expected validation error");
- assertEquals("Name input is empty", error.get());

### Test 13: signUp_pushFailure_returnsError
Intent: sign Up push Failure returns Error.
Expected checks:
- fail("Expected signup error");
- assertEquals("Couldn't Push Values", error.get());

### Test 14: signUp_pushSuccess_callsSuccessAndNotification
Intent: sign Up push Success calls Success And Notification.
Expected checks:
- fail("Expected signup success");
- assertEquals("Success", success.get());
- mockedNotifications.verify(() ->

### Test 15: signUp_queryCancelled_returnsDatabaseError
Intent: sign Up query Cancelled returns Database Error.
Expected checks:
- fail("Expected database error");
- assertEquals("Database error: permission-denied", error.get());

### Test 16: updateProfile_queryCancelled_returnsError
Intent: update Profile query Cancelled returns Error.
Expected checks:
- fail("Expected cancelled error");
- assertEquals("cancelled", error.get());

### Test 17: updateProfile_success_returnsUpdatedMessage
Intent: update Profile success returns Updated Message.
Expected checks:
- fail("Expected update success");
- assertEquals("User Profile Updated", success.get());

### Test 18: updateProfile_userNotFound_returnsError
Intent: update Profile user Not Found returns Error.
Expected checks:
- fail("Expected user not found");
- assertEquals("User not found", error.get());

## UserClassTests

Source file: app\src\test\java\com\example\popin\logicUnitTests\UserClassTests.java

### Test 1: login_correctCredentials_callsOnSuccess
Intent: login correct Credentials calls On Success.
Expected checks:
- assertNotNull(u);
- assertEquals(NAME_IN_DB, u.getName());
- assertEquals(ADDRESS_IN_DB, u.getAddress());
- assertEquals(EMAIL_IN_DB, u.getEmail());
- assertEquals(IS_ADMIN_IN_DB, u.getIsAdmin());
- assertNotNull(UserInSession.getInstance().getUser());
- fail("Expected success, got error: " + message);

### Test 2: login_databaseCancelled_callsOnError
Intent: login database Cancelled calls On Error.
Expected checks:
- @Override public void onSuccess(User u) { fail("Should not succeed on cancel"); }
- @Override public void onError(String msg) { assertEquals("Database error: Connection lost", msg); }
- verify(mockError).getMessage();

### Test 3: login_emailNotFound_callsOnError
Intent: login email Not Found calls On Error.
Expected checks:
- @Override public void onSuccess(User u) { fail("Expected error, got success"); }
- assertEquals("No user with that email/phone number", message);

### Test 4: login_emptyEmail_returnsError
Intent: login empty Email returns Error.
Expected checks:
- @Override public void onSuccess(User u) { fail("Expected error, got success"); }
- assertEquals("Email/Phone input is Empty", message);

### Test 5: login_emptyPassword_returnsError
Intent: login empty Password returns Error.
Expected checks:
- @Override public void onSuccess(User u) { fail("Expected error, got success"); }
- assertEquals("Password input is Empty", message);

### Test 6: login_nullEmail_returnsError
Intent: login null Email returns Error.
Expected checks:
- @Override public void onSuccess(User u) { fail("Expected error, got success"); }
- assertEquals("Email/Phone input is Empty", message);

### Test 7: login_nullPassword_returnsError
Intent: login null Password returns Error.
Expected checks:
- fail("Expected error, got success");
- assertEquals("Password input is Empty", message);

### Test 8: login_wrongPassword_callsOnError
Intent: login wrong Password calls On Error.
Expected checks:
- @Override public void onSuccess(User u) { fail("Expected error, got success"); }
- assertNull(UserInSession.getInstance());
- assertEquals("Incorrect password", message);

### Test 9: testEmptyConstructor
Intent: test Empty Constructor.
Expected checks:
- assertNull(user.getEmail());
- assertNull(user.getPassword());

### Test 10: testSettersAndGetters
Intent: test Setters And Getters.
Expected checks:
- assertEquals("12343", user.getUserID());
- assertEquals("John Smith", user.getName());
- assertEquals("456 Oak St", user.getAddress());
- assertEquals("newPass456", user.getPassword());
- assertEquals("test@example.com", user.getEmail());
- assertEquals("5141234567", user.getPhoneNumber());
- assertEquals("Hello I am John", user.getBio());
- assertTrue(user.getIsAdmin());
- assertEquals(NotificationPreferenceOptions.EMAIL, user.getUserNotificationPreference());
- assertEquals("", user.getPassword());

## UserInSessionTest

Source file: app\src\test\java\com\example\popin\logicUnitTests\UserInSessionTest.java

### Test 1: updateUser_withInstance_replacesCurrentUser
Intent: update User with Instance replaces Current User.
Expected checks:
- assertEquals(second, UserInSession.getInstance().getUser());

### Test 2: updateUser_withoutInstance_doesNothing
Intent: update User without Instance does Nothing.
Expected checks:
- assertNull(UserInSession.getInstance());

### Test 3: userInSessionCanChange
Intent: user In Session Can Change.
Expected checks:
- assertEquals(u, UserInSession.getInstance().getUser());
- assertNull(UserInSession.getInstance());
- assertEquals(u2, UserInSession.getInstance().getUser());

### Test 4: userInSessionCreateAffectsGetInstanceAndGetUser
Intent: user In Session Create Affects Get Instance And Get User.
Expected checks:
- assertEquals(u, UserInSession.getInstance().getUser());

## UserValidationAndFactoryTest

Source file: app\src\test\java\com\example\popin\logicUnitTests\UserValidationAndFactoryTest.java

### Test 1: createUserWithEmail_setsEmailPreferenceAndTrimmedPassword
Intent: create User With Email sets Email Preference And Trimmed Password.
Expected checks:
- assertEquals("user@example.com", user.getEmail());
- assertEquals("", user.getPhoneNumber());
- assertEquals("pass123", user.getPassword());
- assertEquals(NotificationPreferenceOptions.EMAIL, user.getUserNotificationPreference());

### Test 2: createUserWithPhoneNumber_setsSmsPreferenceAndTrimmedPassword
Intent: create User With Phone Number sets Sms Preference And Trimmed Password.
Expected checks:
- assertEquals("", user.getEmail());
- assertEquals("+15145551234", user.getPhoneNumber());
- assertEquals("pass123", user.getPassword());
- assertEquals(NotificationPreferenceOptions.SMS, user.getUserNotificationPreference());

### Test 3: emailAndPhoneValidation_acceptsAndRejectsExpectedFormats
Intent: email And Phone Validation accepts And Rejects Expected Formats.
Expected checks:
- assertTrue(User.isValidEmail("valid.user+tag@example.com"));
- assertFalse(User.isValidEmail("invalid-email"));
- assertFalse(User.isValidEmail(""));
- assertTrue(User.isValidPhoneNumber("+15145551234"));
- assertFalse(User.isValidPhoneNumber("5145551234"));
- assertFalse(User.isValidPhoneNumber(""));

### Test 4: identify_returnsExpectedInputType
Intent: identify returns Expected Input Type.
Expected checks:
- assertEquals(UserInputType.EMAIL, User.identify("person@example.com"));
- assertEquals(UserInputType.PHONE, User.identify("+15145551234"));
- assertEquals(UserInputType.UNKNOWN, User.identify("not-an-email-or-phone"));
- assertEquals(UserInputType.UNKNOWN, User.identify(null));

### Test 5: setters_normalizeEmailPhoneAndPassword
Intent: setters normalize Email Phone And Password.
Expected checks:
- assertEquals("user@example.com", user.getEmail());
- assertEquals("+15145551234", user.getPhoneNumber());
- assertEquals("next-pass", user.getPassword());

### Test 6: setUserNotificationPreference_rejectsInvalidOrMissingContactInfo
Intent: set User Notification Preference rejects Invalid Or Missing Contact Info.
Expected checks:
- assertEquals("Invalid preference selected", noContactUser.setUserNotificationPreference(null));
- assertEquals(
- assertEquals(
- assertNull(noContactUser.getUserNotificationPreference());

