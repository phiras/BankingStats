# BankingStats Rest-API with spring Challenge
# important:
1. I kept modifying and enhacing the design (on the same concepts) even after the deadline , please feel free to browse through the history of commits.

# Getting Started

1. In the base directory of banking folder run maven command:  `mvn package`
2. run maven Command: `mvn spring-boot:run`

## Notes:
1. To achieve O(1) for the /transactions endpoint I had to choose the LinkedList because ArrayList can't guarantee a O(1) for each insertion at the end of it (since it has to automatically double it's size once the array is full).
2. TO achieve O(1) for the /statistics endpoint I have used a class TransactionsStatistics that holds the values and updates its self once a transaction was added or removed
3. Although TreeMap would give log(n) performance for search and remove, I did't use it since it would make the /transaction endpoint execute in long(n) and not O(1) like appending to the beginning of a LinkedList.
4. To keep the state of TransactionStatistics up to date, a thread (StatisticsUpdater) is run in parallel once the spring server starts and the container loads, The thread is controlled by ScheduledExecutorService that wakes it up every 1 second (instead of an expensive infinite while loop)to preform an update to TransactionStatistics which removes the transactions that are older than 60 sec, all of this happens with no collision as I have used the SINGLETON design pattern as synchronized.
5. For the TransactionStatistics I didn't use spring autowired beans and IOC,
I wanted there to be only on instance of TransactionStatistics across all threads and sessions.


## Testing:
1. Unit tests for the TransactionStatistics functions.
3. Integration test for StatisticsUpdater that its working properly it takes 8 sec because time is involved.
2. Integration test for controller end-points.
