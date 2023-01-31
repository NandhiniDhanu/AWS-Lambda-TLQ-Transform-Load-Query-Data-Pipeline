# AWS-Lambda-TLQ-Transform-Load-Query-Data-Pipeline
The project is to implement a multi-stage TLQ pipeline as a set of independent AWS Lambda services.
Sales Database
Sales Data is provided in CSV format. As sample input dataset consists of up to 1.5 million rows and 179
MB of data uncompressed. Data columns include:
Region text
Country text
Item Type text
Sales Channel text
Order Priority text
Order Date date
Order ID integer
Ship Date data
Units Sold integer
Unit Price float
Unit Cost float
Total Revenue float
Total Cost float
Total Profit float

Service #1 (Extract and Transform):
Service #1 either receives the CSV data directly as an input parameter in the data payload (e.g. see REST
multipart), or accesses data using a pointer to a CSV file in S3, or other cloud data service.
Example Service #1 transformations (can implement others):

1. Add column [Order Processing Time] column that stores an integer value representing the
number of days between the [Order Date] and [Ship Date]
2. Transform [Order Priority] column:
L to “Low”
M to “Medium”
H to “High”
C to “Critical”
Page 7 of 12
3. Add a [Gross Margin] column. The Gross Margin Column is a percentage calculated using the
formula: [Total Profit] / [Total Revenue]. It is stored as a floating point value (e.g 0.25 for 25%
profit).
4. Remove duplicate data identified by [Order ID]. Any record having an a duplicate [Order ID] that
has already been processed will be ignored.

Non-Switchboard Architecture: Transformed data should be written out in CSV format and stored in
Amazon S3 or other cloud data service for retrieval by Service #2.

“Switchboard” Architecture: Transformed data should be: (1) persisted locally as a CSV file under /tmp,
(2) stored in memory, and/or (3) persisted to Amazon S3. These alternate data transfer mechanisms
between steps of the TLQ data processing having a “Switchboard” Architecture represent alternate
designs which can be studied. With the “Switchboard” Architecture all services share the same
infrastructure. When Service #2 is called, it may find the cached data in memory or under /tmp leftover
from Service #1. If the data is unavailable, it is requested from Amazon S3. 

Scaling Scenario: If there is just one call to Service #1 to transform the data, but 10 calls to Service #2 to
load the data, using the “Switchboard” Architecture, one call would find the data locally, and 9 calls will
need to request the data from Amazon S3.
Service #2 (Load):
Service #2 requests include a pointer to the transformed CSV data in S3.
Service #2 loads the data from the CSV file into a single table relational database. The table is keyed by
the [Order ID] field which must be unique. Duplicate rows should have been already filtered out by
Service #1.

Database:
There are several options for a “data” tier for a serverless application.
Amazon Aurora is Amazon’s serverless database service. Both a MySQL and PostgreSQL versions are
supported. Our ETL pipeline will perform an initial data transformation (S1), create a relational
representation (S2), and then allow multiple read-only queries to be performed (S3). Since queries in
S3 are read-only, using an external data service is not required.
Use of the locally hosted database SQLite is also a possibility. The advantage is elimination of a
dependency for an external data service for read-only queries. This will keep everyone’s costs down.
The disadvantage is that there are many unsynchronized copies of the database spread across Lambda
functions. Groups may SQLite as a comparison to a serverless backend database (Amazon RDS, etc.)
Synchronization of individual SQLite databases deployed across Lambda functions is not required, as this
would be non-trivial, but could be a good research project.

SQLite:
https://www.sqlite.org/index.html

Groups can propose and adopt alternate backend database approaches and technologies for data
storage and query processing as part of their proposed case study. Design of a serverless application’s
data tier is likely to have a significant impact on overall performance and hosting costs.
For using a local file-based database with the “Switchboard” Architecture, once Service #2 loads data
into a database, such as SQLite, the file can be (1) persisted locally under /tmp in the serverless
container for later use by Service #3. For non-switchboard architectures, Service #2, exports the SQLite
DB file to Amazon S3 for retrieval and replication by Service #3. Groups can devise clever ways to persist
SQLite databases to S3 and pull them down locally when queries run on cold infrastructure.
For simplicity, it is okay to assume that queries will be read only, and that data is only modified during
the load phase of the pipeline. Groups wishing to perform “update” queries in the “Q” phase will run
into the problem of how to synchronize data across Lambda functions.

Service #3 (Query):
Service #3 performs filtering and aggregation of data queries on data loaded into a relational database
by Service #3. Service requests will be in JSON format.
Service #3 is backed by the same SQLite DB (or Amazon Aurora/RDS) to perform meaningful queries to
produce output in JSON array format. Each row will be represented as a single JSON object in an array.
Filtering and aggregation is supported by generating SQL queries.
Each call to Service #3 will specify 1 or more columns to aggregate data on (GROUP BY), and 0 to many
filters which involve including a WHERE clause to an SQL query to specify column matching requests.
Aggregation involves adding a GROUP BY clause to an SQL query and using a function such as SUM(),
AVG(), MIN(), MAX(), and COUNT().
If using a local DB, Service #3 begins by checking if there is a local SQLite DB file saved. If no file exists,
the master copy produced by Service #2 can be downloaded from Amazon S3 and cached to support

Service #3 requests.
Service #3 will accept requests to filter the full data set by column, for example:
- [Region]=“Australia and Oceania”
- [Item Type]=”Office Supplies”
- [Sales Channel]=”Offline”
- [Order Priority]=”Medium”
- [Country]=”Fiji”
Service #3 will support the following data aggregations by column.
- Average [Order Processing Time] in days
- Average [Gross Margin] in percent
- Average [Units Sold]
- Max [Units Sold]
- Min [Units Sold]
- Total [Units Sold]
- Total [Total Revenue]
- Total [Total Profit]
- Number of Orders

Service #3 outputs each row of output from a relational database query as a separate JSON object in a
JSON array. The JSON objects include the data aggregation(s) based on specified filters.
