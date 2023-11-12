Quick info for team:
- core: Stuff that can/will be used by multiple modules (Ballot, User, Common GUI classes, etc)
- tabulation-device: This is the optical scan tabulator device (scans and keeps track of votes)
- voting-device: this is both the "marking device" and the "electronic voting device", which will only differ by whether they keep track of the votes themselves or not.
- voter-information-system: Used to store voter information. Both the tabulation and voting device sections will connect to it via socket to verify voter information.
- simulation: this will be where the simulation starts from and we can start up other modules as needed

Using the maven modules system is new to me, but this allows us to build all components in a single root project and have them depend on one-another. I'm still learning this, so I won't have all the answers to the problems we might encounter.