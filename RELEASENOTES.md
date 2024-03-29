Release notes
=============

2.1.2

* Couchbase client 1.4.4.


2.1.1

* Fix an issue with queries to the view.

2.1.0

* Couchbase client 1.4.1

2.0.0

* CAS 4.0.0
* Couchbase client 1.2.2

1.1.1

* Bugfix: Accessing statistics page with many sessions cause heavy
  load on server.
  https://github.com/KTHse/cas-server-integration-couchbase/issues/1

1.1.0

* CAS 3.5.2.
* Couchbase client 1.1.2
* Simplify and remove unnecessary indexes.

1.0.0

* First major release.
* Added some unit tests.
* Ran some stability tests for a couple of days creating some
  10 000 ticket granting tickets and creating and validating
  some 50 000 service tickets finding no issues.

0.1.2

* Remove extra round trips to database in CouchbaseTicketRegistry.getTickets().

0.1.1

* Fix NPE in statistics view of CAS due to CouchbaseTicketRegistry adding
  null, i.e., tickets that have expired while the list is generated, to
  the response of getTickets().
