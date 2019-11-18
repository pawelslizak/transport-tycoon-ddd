Implementation of:
https://github.com/Softwarepark/exercises/blob/master/transport-tycoon.md

#### EX1

Some motivations behind the design:
- I've tried to make the codebase as small as possible to prevent large refactorings when next
requirements will be unveiled. This caused some methods to gain really big cyclomatic complexity
- I've found that only Warehouses needed identity expressed with a name to allow routing being done 
by third party object - Dispatcher. Another idea would be to treat them as nodes of a graph, so they would 
just hold links to each other ( edges or in our case Routes ) with movement cost. 
At the moment I didn't feel that Factory should be distinguished from other Warehouses.
- This also mean that Cargo can be a ValueObject - it's also my assumption that in game they are just
a plain resource ( like wood, gold in RRS ) not letters or packages witch need uniqueness 
- There is a problem with TransportUnit which does not possess any identity but changes its internal
state by updating current Travel. So its not ValueObject, nor Entity. But I thought that introducing
another concept such as GPS or Tracker to make its state external would be an overkill
- There is also a little overdesign - I assumed that transport on routes can be two way, and also that TransportUnit
coming back to origin Warehouse can be loaded
- Unfortunately business logic leaked to the unit test (JourneySpec) - this is caused by fixing case with 
undeterministic behaviour. Previously TransportUnit had only one method move, which was responsible for 
load, move and unload but that caused different answers depending on invoking sequence of the move method
on TransportUnits. Will be fixed in Ex2 ;).

 