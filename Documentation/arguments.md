```MinePing.jar [Options] -p=<Pinger> -r=<Resolver>```

Executes a rangeping.

Default-Parameters:
===================
Short Name            | Long Name                     | Description                  | Notes
:-------------------- | :---------------------------- | :--------------------------- | :---------------
-h                    | --help                        | Shows this help.             |
-p=&lt;Pinger&gt;     | --pinger=&lt;Pinger&gt;       | Select a pinger.             |
-r=&lt;Resolver&gt;   | --resolver=&lt;Resolver&gt;   | Select a resolver.           | Default: subnet
-w=&lt;Writer&gt;     | --writer=&lt;Writer&gt;       | Select a writer.             | Default: CSV
-s=&lt;Strategy&gt;   | --strategy=&lt;Strategy&gt;   | Select a strategy.           | Default: static
-c=&lt;Connectior&gt; | --connector=&lt;Connector&gt; | Select a connector.          | Default: static
-o=&lt;Output&gt;     | --output=&lt;Output&gt;       | Specifies the outputfile.    |              *1
-l=&lt;Log&gt;        | --log=&lt;Log&gt;             | Specifies the logfile.       |              *1
-t=&lt;Port&gt;       | --port=&lt;Port&gt;           | Specified the port.          |
                      | --extensions=&lt;Dir&gt;      | The Extension-Directory.     |
                      | --flush-results=&lt;Count&gt; | The count of results cached. |
                      | --log-level=&lt;Level&gt;     | Should the output be verbose |


### 1) Special Parameters:
  Value      | Description
  :--------- | :--------------------------------------------------
  *stdout    | Writes the output into the standard output.
  *stderr    | Writes the output into the standard error stream.
  *blackhole | Discards anything written into it.

Preinstalled Resolvers:
=======================
A resolver is a piece of code that provides the ipranges to the software.
There are some preinstalled resolvers in the software.

Default
-------
  The "default" resolver adds a parameter "--range" that can be specified
  multiple times. Each parameter registers a range in a special format
  that allows to specify all ranges.

  CommandName             | Description
  ----------------------: | :-------------------
  --range=&lt;Range&gt;   | A single range.

Subnet
------
  The "subnet" resolver adds a parameter "--range" that can be specified
  multiple times. Each parameter registers a range in the CIDR-Format.

  CommandName             | Description
  ----------------------: | :---------------
  --range=&lt;Range&gt;   | A single range.

Subnet-File
-----------
  The "subnet:file" resovler allows to specify a file with several subnets.
  Each subnet is separated by a file.

  CommandName                | Description
  -------------------------: | :------------------------------------------------
  --range-file=&lt;File&gt;  | The file containing the subnets in CIDR-Notation.


Preinstalled Pingers:
=====================
A pinger is an extension to MinePing that performs the retrival of data of
a server of the range.

Because this can be used in multiple ways, no pinger is defined by this
software.

Preinstalled Writers:
=====================
A writer formats the result of a pinger. For example a file could be written
as a XML-File as well as a CSV-File and a writer can handle some of these
Formats.

CSV
---
  This writer stores the values into a csv-file.

  CommandName                      | Description
  -------------------------------: | :----------------------
  --separator=&lt;Separator&gt;    | The separator char.
  --no-header                      | Don't print a header.

Preinstalled Strategies:
========================
A strategy handles the way how the software is executing its pings.

static
------
  This strategy creates a fixed amount of threads that are executing a ping.

  Commandname                      | Description                             | Notes
  -------------------------------: | :-------------------------------------: | :-----------
  --threads=&lt;Threads&gt;        |    The amount of threads to be created. | Default: 10

pool
----
  This strategy uses a thread pool.

  Commandname                      | Description                                    | Notes
  -------------------------------: | :--------------------------------------------: | :-------
  --max-threads=&lt;Threads&gt;    |    Maximal amount of threads                   |       *1
  --max-requests=&lt;Requests&gt;  |    Maximal amount of requests stored in queue. |

### 1) If the amount of threads is zero, the maximal count of threads is unlimited.

Preinstalled Connectors
=======================
A connector handles in which way the software is connecting to the internet.

static
------
   This connector can create the given amount of sockets at the same time.

   CommandName                  | Description                   | Notes
   ---------------------------: | :---------------------------: | :-----------
   --connections=&lt;Count&gt;  |  Count of parralel connects.  | *1
   --timeout=&lt;Timeout&gt;    |  Timeout in milliseconds.     | 10 Seconds

###1) Possible Connection Parameters:
  CommandName    | Description                                           | Notes
  -------------: | :---------------------------------------------------- | :-----------
     0           |  Dynamically create a new socket within the thread.   |       
    &gt;0        |  Create the given amount of producer threads.         | Default: 10
