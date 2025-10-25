---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# EASync Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2526S1-CS2103T-T11-3/tp/tree/master/src/main/java/seedu/club/Main.java) and [`MainApp`](https://github.com/AY2526S1-CS2103T-T11-3/tp/tree/master/src/main/java/seedu/club/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2526S1-CS2103T-T11-3/tp/tree/master/src/main/java/seedu/club/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `MemberListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2526S1-CS2103T-T11-3/tp/tree/master/src/main/java/seedu/club/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2526S1-CS2103T-T11-3/tp/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Member` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2526S1-CS2103T-T11-3/tp/tree/master/src/main/java/seedu/club/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("deleteMember 1")` API call as an example.

<puml src="diagrams/DeleteMemberSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteMemberCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `ClubBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteMemberCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteMemberCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a member).<br>
   Although this is shown as a single step in the diagram above (for simplicity), it can take several interactions (between the command object and the `Model`) to complete.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `ClubBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddMemberCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddMemberCommand`) which the `ClubBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddMemberCommandParser`, `DeleteMemberCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible, such as during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2526S1-CS2103T-T11-3/tp/tree/master/src/main/java/seedu/club/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the club book data i.e., all `Member` objects (which are contained in a `UniqueMemberList` object).
* stores the currently 'selected' `Member` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Member>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `ClubBook`, which `Member` references. This allows `ClubBook` to only require one `Tag` object per unique tag, instead of each `Member` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2526S1-CS2103T-T11-3/tp/tree/master/src/main/java/seedu/club/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both club book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `ClubBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.club.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedClubBook`. It extends `ClubBook` with an undo/redo history, stored internally as an `clubBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedClubBook#commit()` — Saves the current club book state in its history.
* `VersionedClubBook#undo()` — Restores the previous club book state from its history.
* `VersionedClubBook#redo()` — Restores a previously undone club book state from its history.

These operations are exposed in the `Model` interface as `Model#commitClubBook()`, `Model#undoClubBook()` and `Model#redoClubBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedClubBook` will be initialized with the initial club book state, and the `currentStatePointer` pointing to that single club book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th member in the club book. The `delete` command calls `Model#commitClubBook()`, causing the modified state of the club book after the `delete 5` command executes to be saved in the `clubBookStateList`, and the `currentStatePointer` is shifted to the newly inserted club book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new member. The `add` command also calls `Model#commitClubBook()`, causing another modified club book state to be saved into the `clubBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitClubBook()`, so the club book state will not be saved into the `clubBookStateList`.

</box>

Step 4. The user now decides that adding the member was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoClubBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous club book state, and restores the club book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial ClubBook state, then there are no previous ClubBook states to restore. The `undo` command uses `Model#canUndoClubBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoClubBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the club book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `clubBookStateList.size() - 1`, pointing to the latest club book state, then there are no undone ClubBook states to restore. The `redo` command uses `Model#canRedoClubBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the club book, such as `list`, will usually not call `Model#commitClubBook()`, `Model#undoClubBook()` or `Model#redoClubBook()`. Thus, the `clubBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitClubBook()`. Since the `currentStatePointer` is not pointing at the end of the `clubBookStateList`, all club book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire club book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the member being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile: Student Club Manager**

* has a need to manage a significant number of members' and participants' contacts and events
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**:

EASync provides a student club manager with a centralised system to efficiently manage members’ and participants’ contact information and events, helping them stay organised. Optimised for users who prefer typing as input, it simplifies management of roles, relationships, and member turnover without relying on slow, cluttered spreadsheets or manual tracking.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                       | I want to …​                                                                       | So that I can…​                                                |
|----------|-------------------------------|------------------------------------------------------------------------------------|----------------------------------------------------------------|
| `*`      | new club manager              | easily purge sample data                                                           | start fresh with my own club info                              |
| `*`      | club manager                  | detect duplicate entries (based on name, email, missing fields)                    | keep my contact lists accurate                                 |
| `*`      | club manager                  | separate clubs by tabs                                                             | clubs do not get mixed up                                      |
| `*`      | club manager                  | view a summary of contact health (e.g. duplicates, missing fields)                 | clean up when needed                                           |
| `*`      | club manager                  | add additional notes to specific members (e.g. dietary restrictions/photo consent) | plan events smoother                                           |
| `*`      | club manager                  | simulate role reassignments before committing                                      | plan transitions without disrupting current setups             |
| `*`      | club manager                  | export an event's participant list with roles                                      | share it with my committee                                     |
| `*`      | club manager                  | export a member's timeline and role history                                        | prepare handover documents or performance reviews              |
| `*`      | experienced club manager      | chain commands (e.g. addMember && assignRole)                                      | execute multiple actions in one go                             |
| `*`      | experienced/lazy club manager | use keyboard shortcuts for repetitive tasks                                        | save time                                                      |
| `*`      | experienced/lazy manager      | look at my command history                                                         | quickly reuse the previous command without retyping            |
| `*`      | lazy club manager             | press on a member's email address to create a new email                            | send emails quickly without having to copy their email address |
| `*`      | impatient club manager        | press tab to autocomplete commands                                                 | complete what i need to do faster                              |
| `*`      | club manager                  | undo the last command                                                              | recover from any mistake                                       |
| `* *`    | new club manager              | explore sample data                                                                | understand how EASync works before committing                  |
| `* *`    | club manager                  | edit members' memberal details                                                     | update them when information changes                           |
| `* *`    | club manager                  | restore archived members                                                           | re-engage them if they return                                  |
| `* *`    | club manager                  | validate contact fields (e.g., missing email, malformed tags)                      | catch errors before they affect workflows                      |
| `* *`    | club manager                  | search for members by their name, email, role, or by events                        | find them quickly                                              |
| `* *`    | club manager                  | check the attendance of the club’s members                                         | keep track of their overall club participation                 |
| `* *`    | club manager                  | bulk edit members details (eg. membership status)                                  | update records efficiently                                     |
| `* *`    | club manager                  | archive members who are no longer active                                           | keep my workspace focused on current contributors.             |
| `* *`    | club manager                  | view current role distribution across events                                       | identify over- or under-utilized members                       |
| `* *`    | club manager                  | attach notes to a member profile                                                   | remember preferences, strengths, or past issues                |
| `* *`    | club manager                  | be informed of duplicate events or members when adding new ones                    | keep member/event lists clean                                  |
| `* *`    | busy/forgetful club manager   | be highlighted to important or upcoming events                                     | keep track of my schedule                                      |
| `* *`    | club manager                  | check upcoming events/list events chronologically                                  | remember what events are happening when                        |
| `* *`    | new club manager              | see a list of all possible commands                                                | know how to carry out what I want to do with the app           |
| `* * *`  | club manager                  | add a new member’s memberal details such as name, phone number, and email address  | keep track of member information                               |
| `* * *`  | club manager                  | view a list of all members                                                         | check who is part of the club                                  |
| `* * *`  | club manager                  | delete members                                                                     | remove members who have left the club                          |
| `* * *`  | club manager                  | tag members with appointed roles                                                   | identify and keep track of key appointment holders             |
| `* * *`  | club manager                  | create events with dates, participant lists, and details                           | easily manage club events                                      |
| `* * *`  | club manager                  | delete events                                                                      | remove events that are cancelled or have already passed        |
| `* * *`  | club manager                  | backup my club book                                                             | avoid losing data when I leave the app                         |

### Use cases

(For all use cases below, the **System** is `EASync` and the **Actor** is the `user`, unless specified otherwise)

**UC01: Add a new Event**

**Preconditions**
* App is open

**Guarantees**
* Addition of new event will not affect other events in existing list of events
* If a new event is added, it will be displayed at the top of the list of events

**MSS**
1. User requests to add an event
2. System displays a success message
3. System creates a new event
4. System displays the new event created in the list of events

    Use case ends

**Extensions**

* 1a. One or more of the parameters are missing

    * 1a1. System informs user of missing fields

      Use case ends

* 1b. One or more of the parameters are of invalid format

    * 1b1. System informs user of incorrect fields

      Use case ends

* 1c. Event with the same name, to and from datetimes are entered in

    * 1c1. System informs user of duplicated event

      Use case ends

**UC02: Adding a Member**

**Preconditions**
* App is open

**Guarantees**
* Addition of new member will not affect other members in existing list of members

**MSS**
1. User requests to add member
2. System displays success message
3. System displays the new member in the member list

   Use case ends

**Extensions**

* 1a. Missing required parameters in member description (e.g. name, phone)

    * 1a1. System informs user of missing field(s)

      Use case ends

* 1b. Member with the same name, email address, and phone number as an existing member entry is entered in

    * 1b1. System informs user of duplicated member

      Use case ends

**UC03: Assigning Member to an Event**

**Preconditions**
* The member to be assigned must be in the list of members
* The event to be assigned to must exist in the list of events

**Guarantees**
* Member will be displayed with the assigned event tag

**MSS**

1.  User requests to assign a member to a certain event
2.  System displays success message
3.  System displays the member with the event tag

    Use case ends

**Extensions**

* 1a. Missing required parameters in command
    * 1a1. System informs user of missing fields

      Use case ends

**UC04: Tag Member with Event Role**

**Preconditions**

* The member to be tagged must exist in the list of members
* The event must exist in the list of events
* Member is already assigned to desired event (UC03)

**Guarantees**

* Member has a role for the event

**MSS**

1. User requests to tag a member to a certain event role
2. System displays success message
3. System displays member with role for the event

   Use case ends

**Extensions**

* 1a. Missing required parameters in command (e.g. member index, event name, role name)
    * 1a1. System informs user of missing fields

      Use case ends

* 1b. Incorrect parameters within command (e.g. member index out of range, event does not exist, role does not exist within the event)
    * 1b1. System informs user of incorrect parameters

      Use case ends

* 1c. Member already has the role for the event tagged to him/her already
    * 1c1. System informs user of invalid action

      Use case ends

**UC05: Tag Member with Club Role**

**Preconditions**

* The member to be tagged to must exist in the list of members

**Guarantees**

* Member has a club role

**MSS**

1. User requests to tag a member to a certain club role
2. System displays success message
3. System displays member updated with club role

   Use case ends

**Extensions**

* 1a. Missing required parameters in command (e.g. member index, role name)
    * 1a1. System informs user of missing fields

      Use case ends

* 1b. Incorrect parameters within command (e.g. member index out of range)
    * 1b1. System informs user of incorrect parameters

      Use case ends

**UC06: Removing a Member**

**Preconditions**
* Member to be removed must exist in the list of members

**Guarantees**
* Member will be removed from the list of members
* Updated member list will be displayed

**MSS**
1.  User requests to list members
2.  System shows a list of members
3.  User requests to remove a member
4.  System deletes the member
5.  System displays success message and member list without the removed member

    Use case ends

**Extensions**

* 2a. The list is empty

  Use case ends

* 3a. System detects an error in the command (e.g., missing or incorrect fields)

    * 3a1. System shows an error message

      Use case resumes at step 2

**UC07: Removing an Event**

**Preconditions**

* The event to be removed to must exist in the list of events

**Guarantees**

* The event is removed from the list of events

**MSS**

1. User requests to remove an event
2. System displays success message
3. System displays event list without the removed event

    Use case ends

**Extensions**

* 1a. Missing required parameters in command (e.g. event index)
    * 1a1. System informs user of missing fields

      Use case ends

* 1b. Incorrect parameters within command (e.g. event index out of range)
    * 1b1. System informs user of incorrect parameters

      Use case ends

**UC08: Displaying an event’s details**

**Preconditions**

* The event must exist

**Guarantees**

* Displays a specific event’s member roster

**MSS**

1. User requests to display a specific event
2. System displays success message
3. System displays event’s details and member roster

    Use case ends

**Extensions**

* 1a. Missing required parameter in command (e.g. event index)

    * 1a1. System informs user of missing field

        Use case ends

* 1b. Incorrect parameter within command (e.g. event index out of range)

    * 1b1. System informs user of incorrect parameter

        Use case ends

**UC09: Assigning a member to an event**

**Preconditions**

* The member and event must exist
* The member must not have been already assigned to the event

**Guarantees**

* The member will be added to the event’s roster

**MSS**

1. User requests to assign a member to an event
2. System displays success message
3. System updates event’s roster with the assigned member
4. System displays event’s details(UC08)
        Use case ends

**Extensions**

* 1a. Missing required parameters in command (e.g. member name)

    * 1a1. System informs user of missing fields

        Use case ends

* 1b. Invalid parameters within command (e.g. blank or non-alphanumeric characters)

    * 1b1. System informs user of invalid parameters

        Use case ends

* 1c. Member is already assigned to the event

    * 1c1. System informs user that the member is already assigned to the event

        Use case ends

* 1d. Event role is specified in user request and event role exists under the event

    * 1d1. System displays success message 
    * 1d2. System displays event’s roster with the assigned member, with the event role added to the member

        Use case ends

* 1e. Event role is specified in user request but event role does not exist under the event

    * 1e1. System informs user that the event role does not exist under the event

        Use case ends

**UC10: Unassigning a member from an event**

**Preconditions**

* The member and event must exist
* The member must have already been assigned to the event

**Guarantees**

* The member will be removed from the event’s roster
* Any event roles that the member may have under that event will also be removed

**MSS**

1. User requests to unassign a member from an event
2. System displays success message
3. System displays event’s roster with the unassigned member removed
        Use case ends

**Extensions**

* 1a. Missing required parameters in command (e.g. member)

    * 1a1. System informs user of missing fields

        Use case ends

* 1b. Invalid parameters within command (e.g. blank or non-alphanumeric characters)

    * 1b1. System informs user of invalid parameters

        Use case ends

* 1c. Member or event does not exist

    * 1c1. System informs user that the member or event cannot be found

        Use case ends

**UC11: Unassigning an event role from a member**

**Preconditions**
* The member and event must exist 
* The member must have already been assigned to the event 
* The member must have an event role related to that event

**Guarantees**

* The event role will be removed from the member’s event roles list

**MSS**

1. User requests to unassign an event role from a member
2. System displays success message
3. System displays member list with the member’s event role removed
        Use case ends

**Extensions**

* 1a. Missing required parameters in command (e.g. event role)

    * 1a1. System informs user of missing fields

        Use case ends

* 1b. Invalid parameters within command (e.g. blank or non-alphanumeric characters)

    * 1b1. System informs user of invalid parameters

        Use case ends

* 1c. Member, event, or event role does not exist

    * 1c1. System informs user that the member, event, or event role cannot be found

        Use case ends


### Non-Functional Requirements

1. Scalability
   1. The system must support managing at least 100 members and 50 events without noticeable degradation in performance by the user.
2. Portability
   1. The system must be able to run on Windows, macOS, and Linux as long as Java 17 is installed.
   2. The system must run without requiring a separate installer. A single distributable JAR must be sufficient to run the program.
3. Reliability
   1.  The system shall remain operational without unexpected termination under all valid user operations and shall provide descriptive error messages for at least 90% invalid inputs tested.
4. Data Integrity
   1. The system must automatically save to local storage after each successful command by the user to prevent data loss to a maximum of 30 seconds of user activity in the event of an application crash or unexpected closure.
   2. Data must be stored locally in a human-readable and editable text file format, as JSON.
   3. The application must function without relying on any remote server to operate.
5. Performance
   1. All user commands (e.g., addEvent, deleteMember) should be processed within 1 second under normal usage.
   2. The system should complete data loading from local storage within 2 seconds on startup.
   3. The executable JAR should operate using less than 100MB of memory under normal usage (e.g., with 100 events and 50 members).
   4. Assets (e.g., images, libraries) must not be unnecessarily large or included unless strictly required
6. Usability
   1. Command parameters should only be defined format (e.g. n/, f/, dt/) for consistency.
   2. All commands must be able to be completed by the user using only typed commands.
      GUI interaction  must be secondary, rather than being the primary input mode.
7. Single-User Model
   1. The product must only support one user. Data files must not be shared or accessed concurrently by multiple users.
8. Screen Resolution Compatibility
   1. The GUI must be clearly visible and easily clickable at 1920×1080 resolution (100% and 125% scaling).
   2. The GUI must remain visible at 1280×720 resolution (150% scaling).
9. PDF-Friendly Documentation
   1. All project documentation (DG/UG) must be exportable and viewable as static PDFs without expandable panels, embedded media, or interactive elements.


### Glossary

* **Member**: A member who is part of the club and participates in club activities and events.
* **Event**: A scheduled activity organised by the club with a name, date/time, and optional details.
* **Role**: A responsibility or position a member can hold for the club or an event (e.g., “Logistics”, “Treasurer”, "MC").
* **Command**: A structured text instruction typed by the user following the app’s syntax (e.g. addEvent, deleteMember).
* **Command Line**: A space for typing text commands, serving as the main way for the user to interact with the application.
* **Index**: A unique number representing the position of a member or event in the displayed list. It is 1-based (starts from 1).
* **Parameter**: A short identifier placed before input in a command to indicate the information type (e.g., n/ for name,  f/ for from-date).
* **JAR (Java Archive)**: The file format used to package and deliver the application, containing everything needed for it to run.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a member

1. Deleting a member while all members are being shown

   1. Prerequisites: List all members using the `list` command. Multiple members in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No member is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
