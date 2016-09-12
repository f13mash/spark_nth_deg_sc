# spark_nth_deg_sc

To calculate connections uptill nth degree, program uses spark(sql) and runs iteratively each time extending (k)th degree with 1-degree connection to compute connections till (k + 1)th-degree

Example. Connection degree = 3
As input we already have data for connection degree  = 1
We convert the input into a list(table) of uni-direction relation. Using a bi-directional is more space-optimized but adds up complexity in the logic
A connection 'a' and 'b' is stored as (lets call the table connection_list_1)
  ______
  |u1|u2|
  -------
  |a |b |
  |b |a |
  -------

To calculate degree k=2, we take the connection for k-1 and extend it with 1 degree connection from connection_list_1. So basically, this results in a join between connection_list_1 and connection_list_1. Resultant connection list is called connection_list_2
For k = 3, we use connection_list_2 and connection_list_1, and perform join operation again.


Program runs O(N) spark jobs, where N = connection degree, and it can be optimized to use only O(lg(N)) spark jobs, but that would mean persisting much larger data set in memory/disk.
In the current approach, as connection_list_1 is used at all stages, it is cached(and can further be broadcasted depending on data-size for further speedup)


Dependencies
Scala 2.11
SBT
inside the project directory run, to build the project
sbt clean compile
to run the project, 
sbt "run feed/user_conn.csv 4" #feed/user_conn.csv is a sample feed file, and are calculating till 4th degree

src/main/scala-2.11/Main.scala is the entry-point app
src/main/scala-2.11/services/UserConn contains the main logic of building and executing the spark jobs


