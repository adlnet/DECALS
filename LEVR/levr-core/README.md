levr-core
=========

levr-core is part of the open source project DECALS (Data for Enabling Content in Adaptive Learning Systems) that was developed under Broad Area Announcement ADL BAA 12-001 for ADL (Advanced Distributed Learning Initiative) by Eduworks.

Base package for Levr product. This project contains the languages and base architecture for LEVR.

LEVR is a web service framework that allows you to write and publish complex web services quickly and in a variety of domain-specific languages.

One of the first languages we wrote to perform this is a functional language called RS2. Examples of this language can be found here: https://github.com/Eduworks/levr-scripts -- and a definition of the language in ANTLR can be found in the project.

* All libraries that derive levr-core are to contain libraries of Resolvers or Crunchers, and be published under the derived moniker.
* Languages are to be defined in ANTLR format, and are contained within levr-core. These languages will define web services as their endpoints.
* The preferred method of publishing web services is through Java Servlets.

levr-core is Open Source under the Apache 2.0 license, the details of which can be found in LICENSE.txt.

levr-core is under active development. It is released only as part of other projects, and you will not find JAR releases here. If this is desired, contact one of our developers.

levr-core contains support for the following languages, all of which will be represented as examples in levr-scripts (eventually).

* RS2
* Javascript
* Python

levr-core requires the following to build or use:

* Java 1.6 or above. (1.6.0_26 recommended)
* ANT (for building)
* IVY (for fetching libraries from Maven)
* eduworks-common, found at https://github.com/Eduworks/eduworks-common

The bug tracker for levr-core is being currently maintained on GitHub, at the following url: https://github.com/Eduworks/levr-core/issues

The lead contact for levr-core is Fritz Ray of Eduworks (fritz.ray@eduworks.com).

This and the LEVR source has enjoyed the contributions from the following people:

	Tom Wrench (Retired Eduworks)
	Martin Hald (Retired Eduworks)
	Ronald "Fritz" Ray (Eduworks)
	Aaron Veden (Eduworks)
	Daniel Harvey (Retired Eduworks)
	Devlin Junker (Eduworks)

The code has been enhanced in part or used in the following projects, thanks to the U.S. Government.

	UCASTER, NSF Grant 1044161
	TRADEM, DOD SBIR W911-QX-12-C-0055
	ASPOA, NSF Grant 1214187
	TRADEM, DOD SBIR W911-QX-13-C-0063
	DECALS, ADL/DOD Project W911-QY-13-C-0087
	PULSAR, ADL/DOD Project W911-QY-13-C-0030 Subcontract 13-0030-8975-01
	ASPOA, NSF Grant 1353200