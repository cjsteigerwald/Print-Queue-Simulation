# Print-Queue-Simulation
<div
<a href="https://cloud.githubusercontent.com/assets/9287625/7329277/4a4f1912-ea92-11e4-893d-82430623f40e.jpg" target="_blank">
<img src="https://cloud.githubusercontent.com/assets/9287625/7329285/63960246-ea92-11e4-9293-d437cb630f96.jpg" width="100" height="131" border="0" alt="Thumbnail 2" />
</a>

<a href="https://cloud.githubusercontent.com/assets/9287625/7329296/840ea3a2-ea92-11e4-86e9-947fb8c54fb2.jpg" target="_blank">
<img src="https://cloud.githubusercontent.com/assets/9287625/7329305/92fe8cc4-ea92-11e4-997c-2785cea80f01.jpg" width="100" height="131" border="0" alt="Thumbnail 3" />
</a></div>
Printer Queue Simulation GUI based
Edmonds Community College CS 142
Completed 6/5/2013

<b>Project jar file located: <a href="https://github.com/cjsteigerwald/Print-Queue-Simulation/blob/master/dist/Print_Queue_Simulation_Final.jar">Here</a><br>
Project .java files located: <a href="https://github.com/cjsteigerwald/Print-Queue-Simulation/tree/master/src/print">Here</a>

<strong><bold>Problem</strong><bold><br><br>
Write a program that solves a classic computer science problem known as
client/server problem for print jobs and printers at Alderwood Hall.
Assume  there are four servers: 
<UL>
<LI>Printer A
<LI>Printer B
<LI>Printer C
<LI>Printer D
</UL>
Each printer has a different print rate, expressed as a percentage.
For example, Printer A has a print rate of 89, meaning it averages
0.89 page per second.  Each printer's print job also has a print
rate.  For examble, on Job #1, Printer A has a print rate of 84,
meaning that it is printing 0.84 page per second on that job.  A 
single printer's print rates vary among jobs because of other factors
such as network traffic.  For example, Printer B, which has an average
print rate of 0.97 page per second, prits Joab #2 at 0.91 pps (pages per
second) and Job #6 at 0.95 pps.  The print jobs arrive at the print queue
at random times.<br><br>
When jobs arrive faster than the printers can begin printing them, they
accumulate in the print queue.  When all printers are busy, print jobs
accumulate in the print queue.  When a a printer completes a job it 
will begin processessing a new print job.<br><br>

<strong><b>Solution</strong></b><br><br>
<OL>
<LI><a href="https://github.com/cjsteigerwald/Print-Queue-Simulation/blob/master/src/print/PrinterGUI.java">PrintQueueSimulationGUI</a>-- the simulation driver class.  A GUI based
JFrame that allows the user to set/select number of servers (printers), 
set/select mean interarrival time, and set/select mean duration of each
job.
<LI><a href="https://github.com/cjsteigerwald/Print-Queue-Simulation/blob/master/src/print/Client.java">Client</a>-- Each print job has an instance of this class. It contains 
a random number generator randomJobSize that generates the exponentially
distributed job sizes with a mean of 100 pages. It is declared static 
because only one instance is needed to produce all the job sizes. Similarly, 
the static int nextId is used to generate identification numbers for all the
jobs. The constructor uses the next Id counter to set the job id, and it uses 
the randomJobSize generator to set the jobSize. Then it prints one line of
output, announcing that that job has arrived. The beginService () method
assigns the server reference to the printer that invoked it and then prints
one line of output, announcing that the printing has begun. Similarly, the
endService() method nullifies the server reference after printing one line
of output that announces that the printing has ended.

<LI><a href="https://github.com/cjsteigerwald/Print-Queue-Simulation/blob/master/src/print/Server.java">Server</a>-- It has a random number generator randomMeanServiceRate that 
generates the normally distributed rates with mean 100.0 and standard 
deviation 20.0. It produces the meanServiceRate for each printer. In the
run just shown, it produced the rates 89 for Printer A, 97 for Printer B,
106 for Printer C, and 128 for Printer D. Similarly, the random number 
generator randomServiceRate generates the normally distributed rates for 
each print job. In the run just shown, it produced the rates 84 for Job #1,
87 for Job #3, and 92 for Job #5. Those came from a normal distribution with
mean 89 (for Printer A). The standard deviation is set at 10 for each 
printer's distribution. The beginServing () method assigns the client 
reference to the client job that it is printing and obtains the normally
distributed serviceRate from the randomServiceRate generator. Then it sends
the beginService message to its client print job. Next, the assignment int 
serviceTime = (int)Math.ceil(client.getJobSize()/serviceRate); computes the
time (number of seconds) that it will take to do the print job by dividing
the job size (the number of pages) by the printing rate (pages per second).
The integer ceiling of this ratio is used as a count of the number of seconds
to elapse. This count is then added to the current time to initialize the
timeServiceEnds field of the Server object.

<LI><a href="https://github.com/cjsteigerwald/Print-Queue-Simulation/blob/master/src/print/Random.java">Random</a>-- extends java.utilRandom. Given above.

<LI><a href="https://github.com/cjsteigerwald/Print-Queue-Simulation/blob/master/src/print/ClientQueue.java">Queue</a>--an interface that extends Collection. Public method include 
enqueue, dequeue, getBack, and getFront. You may decide to implement the
Queue class differently--it need not be an interface.

<LI><a href="https://github.com/cjsteigerwald/Print-Queue-Simulation/blob/master/src/print/ClientQueue.java">Client Queue</a>--extends List.

<LI><a href="https://github.com/cjsteigerwald/Print-Queue-Simulation/blob/master/src/print/Splash.java">Splash</a> screen with which the program begins and an About form which
describes the project among other info (copyright, warning, logo, etc.).

<LI>A sound data structure of the Person class to hold the preference data. 
This could be an array, ArrayList, LinkedList (preferable), HashMap, or any
other structure you desire.

<LI><a href="https://github.com/cjsteigerwald/Print-Queue-Simulation/tree/master/dist/javadoc">Javadocs</a>, description of the program, and comments, comments everywhere.

<LI>Menus that synchronize with corresponding buttons and with at least the 
following menu choices:
<UL>
<LI>File with Open, Clear, Print, Save, and Exit menu items.
<LI>Statistics displaying all averages
<LI>Help with About menu item for an About form.
</UL>
<LI>The project should start with a Splash Screen that closes itself after
so many seconds and it should contain an About form activated from the Help menu.
</OL>
&nbsp;

