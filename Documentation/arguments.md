MinePing.jar
    ```<REWRITE>```

Executes a rangeping.

Default-Parameters:
===================
-h              | --help                  | Shows this help. |

-p=<Pinger>     | --pinger=<Pinger>       | Select a pinger.
-r=<Resolver>   | --resolver=<Resolver>   | Select a resolver.  | Default: subnet
-w=<Writer>     | --writer=<Writer>       | Select a writer.    | Default: CSV
-s=<Strategy>   | --strategy=<Strategy>   | Select a strategy.  | Default: static
-c=<Connectior> | --connector=<Connector> | Select a connector. | Default: static

-o=<Output>     | --output=<Output>       | Specifies the outputfile. |        *1
-l=<Log>        | --log=<Log>             | Specifies the logfile.    |        *1

-t=<Port>       | --port=<Port>           | Specified the port. |

                | --extensions=<Dir>      | The Extension-Directory. |
                | --flush-results=<Count> | The count of results cached. |
                | --log-level=<Level>     | Should the output be verbose |


### 1) Special Parameters:
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
  --range=<Range>   |    A single range.

Subnet
------
  The "subnet" resolver adds a parameter "--range" that can be specified
  multiple times. Each parameter registers a range in the CIDR-Format.
  --range=<Range>   |    A single range.

Subnet-File
-----------
  The "subnet:file" resovler allows to specify a file with several subnets.
  Each subnet is separated by a file.
  --range-file=<File>  | The file containing the subnets in CIDR-Notation.


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
  --separator=<Separator>    |    The separator char.
  --no-header                |    Don't print a header.

Preinstalled Strategies:
========================
A strategy handles the way how the software is executing its pings.

static
------
  This strategy creates a fixed amount of threads that are executing a ping.
  --threads=<Threads>        |    The amount of threads to be created.

pool
----
  This strategy uses a thread pool.

  --max-threads=<Threads>    |    Maximal amount of threads        |       *1
  --max-requests=<Requests>  |    Maximal amount of requests stored in queue. |

### 1) If the amount of threads is zero, the maximal count of threads is unlimited.

Preinstalled Connectors
=======================
A connector handles in which way the software is connecting to the internet.

static
------
   This connector can create the given amount of sockets at the same time.

   -c=<Count>  |  --connections=<Count>  |  Count of parralel connects.  |   *1
               |  --timeout=<Timeout>    |  Timeout in milliseconds.     |

###*1 Possible Connection Parameters:
     0        |  Dynamically create a new socket within the thread.   |       
    >0        |  Create the given amount of producer threads.         | Default: 10
