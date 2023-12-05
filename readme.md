# Home Safe
This project is a group project assigned by the
UNM CS 460 - Software Engineering class. The project
repository represents the software of our Voting System, and its simulation.

## Team
- Ester Aguilera: Team Manager, Frontend Developer
- Raju Nayak: Frontend Developer
- Emely Seheon: Frontend Lead
- Jacob Graves: Document Manager, Backend Developer
- Majil Man Pradhan: Backend Developer
- Michael Millar: Software Architect, Backend Lead

**Team Roles**
To see a more specific detailing of who worked on what task,
see our task distribution [here](https://unmm-my.sharepoint.com/:x:/g/personal/eaguilera_unm_edu/EdHDYDYUNBRNvJJSK7iGHbUBerz3oefQoILu7fMGqvlyJg?e=FsRUnT):

More Generally:
- Ester Aguilera:
1. Voter Registration button GUI functions
2. Staff buttons GUI functions

- Raju Nayak:
1. Tabulation button GUI functions
2. Report generation

- Emely Seheon:
1. General Electronic Voting button GUI functions
2. Paper ballot

- Jacob Graves
1. Graph Database
2. Vote tabulation on backend

- Manjil Man Pradhan
1. User class
2. Authentication

- Michael Miller
1. Code structure
2. General backend structure

## Project Usage
**Electronic Voting**

- This option can not be accessed while the polls are closed
- Allows a registered voter to vote either with a Marking Device or through Electronic Voting
1. Enter SSN to verify you are eligible to vote
2. Choose to vote with either the marking device or through electronic voting
3. Vote
4. Submit vote
5. If voted through the marking device, choose "Scan Paper Ballot" to scan your ballot.

**Tabulator**

- Allows a voter to scan their vote when voting through paper or by the marking device. 
- When voting through paper, fill out a paper ballot, then click "Scan Paper Vote"
1. To tabulate results, the polls must be closed.
2. Login as a staff member.
3. Tabulate results
4. Reports are automatically printed.

**Voter Registration**

-Allows an eligible user to register to vote.

**Staff**

- Gives the ability to create a staff member and open and close the polls.
- To open the polls:
1. Login as an admin staff member
2. Choose an election schema to produce the ballots.

### IDE Usage
This project is runnable within the IntelliJ IDE without
additional configuration. Simply run the main class:
`src/main/java/edu/unm/App.java`.

### Maven
This project was built using Apache Maven.

**Remove Build Artifacts:** `mvn clean`

**Install dependencies:** `mvn install`

**Run tests:** `mvn test`

**Compile jar:** `mvn package`