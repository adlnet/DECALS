## Table of Contents

+ [DECALS](#1_0)
+ [Results of the DECALS Project](#2_0)
+ [Instructions for Setting up DECALS, and the Competency System UI](#3_0)
    * [Code projects in this Github repository required to build DECALS](#3_0_1)
    * [Prerequisites](#3_0_2)
    * [VMware VM settings](#3_1)
    * [Install Ubuntu OS](#3_2)
    * [Install Eclipse Development Environment](#3_3)
    * [Install Java 7](#3_4)
    * [Install Java JDK](#3_5)
    * [Install Apache Ant/Git](#3_6)
    * [Install Apache Tomcat 7](#3_7)
    * [Install Apache Solr DBs](#3_8)
    * [Setup Apache Solr DB for DECALS DB](#3_8_1)
    * [Setup Apache Solr DB for LR Registry DB](#3_8_2)
    * [Download the source code from this repository](#3_9)
    * [Setting up and building the decals-ui source project](#3_10)
    * [Setting up the decals-ui and EwGWTLib projects](#3_11)
    * [To build the decals-ui project](#3_11_1)
    * [Building the LEVR components](#3_12)
    * [Installing LEVR scripts](#3_13)
    * [Creating a location for the database files](#3_14)
    * [Install and configure the decals-ui project](#3_15)
    * [Open decals/js/installation.settings for edit](#3_15_1)
    * [Open /var/lib/tomcat7/etc/decals.competency.config.rs2 for edit](#3_15_2)
    * [Open /var/lib/tomcat7/etc/decals.config.rs2 for edit](#3_15_3)
    * [Installing the competency-ui source](#3_16)
    * [Open /var/lib/tomcat7/webapps/ROOT/competency/index.html for edit](#3_16_1)
    * [Open /var/lib/tomcat7/webapps/ROOT/competency/js/definitions.js for edit](#3_16_2)
    * [Final Steps](#3_17)
+ [Contributing to the Project](#CTTP)
+ [License](#License)


# <a name="1_0"></a>DECALS
*DECALS, stands for Data for Enabling Content in Adaptive Learning Systems. DECALS was a follow-on project to RUSSEL with disparate high-level goals, all of which were achieved during the two-year project*.

**GOAL 1**: Improve search in the LR by adding “semantic search” capabilities.

**GOAL 2**: Enable authorized users to upload their own content to the LR. The LR contains pointers to content and metadata, but not the actual content. DECALS allows users who are interacting with the LR to store content in a persistent repository and make it discoverable in the LR.

**GOAL 3**: Provide content curation services to ADL’s Personal Assistant for Learning (PAL) projects.

**GOAL 4**: Define and implement decals that encode information about learning activities in a way that adaptive learning systems (e.g. PALs) can interpret and use to select or recommend activities.

**GOAL 5**: Evaluate whether adapting search to basic user preferences can improve learning. This addresses the question: “How much adaptation is really needed to increase effect sizes?”

**GOAL 6**: Improve the architecture and feature set of RUSSEL.

## <a name="2_0"></a>Results of the DECALS Project
In achieving the goals stated on the previous page the DECALS team produced the following results:

**RESULT 1**: DECALS Application: The DECALS application turns the LR into a repository that learners and instructors can use to find resources matched to learning goals and preferences and to add additional relevant resources. It serves as a web-based search interface to the LR and provides a means for users to store resources and register them in the LR. DECALS includes RUSSEL’s repository capabilities, innovative search and discovery features, a competency management system, and the ability to produce “decals,” explained below.
  
**RESULT 2**: Evaluation: DECALS search was evaluated for usability and for its applicability as a learning tool. The results of this evaluation, which were positive, are reported separately.

**RESULT 3**: Content Curation: DECALS implements a web service infrastructure that exposes DECALS functionality so that other PALs and adaptive learning systems can use DECALS to manage and curate resources. DECALS includes content management services, meta-tagging services, and competency management services. DECALS is architected to allow third-party services such as machine-learning-based competency alignment services to be added as well.

**RESULT 4**: Competency Management: Most adaptive learning systems maintain information on the competencies (skills, knowledge, and abilities) that individuals have mastered or desire to master. Although not in the original scope of DECALS, it became apparent early in the project that PAL systems must maintain ontologies of competencies and store competency-based learner records. The DECALS team developed a competency management system that meets these requirements. The competency system can be accessed through a user interface or through a set of APIs. It uses information models derived from existing standards and modified by the DECALS team with input from an ADL competency alignment working group.

**RESULT 5**: Decals: DECALS tackled the problem of tagging content with metadata that is not only for human consumption but can also be interpreted by PAL systems and used to select or recommend learning interventions. These tags are called decals. Several possible formats for decals were researched. The format finally used is based on www.schema.org. It uses the JSON-LD representation of existing schema.org elements to represent decals and to associate them with resources and is documented at [http://repo.decals.eduworks.com](http://repo.decals.eduworks.com). 

**RESULT 6**: RUSSEL Updates: To alleviate issues with the original Alfresco-based RUSSEL, the DECALS team re-architected RUSSEL to use a NoSQL database and web service middleware called LEVR™. RUSSEL functionality was then ported from the original RUSSEL implementation, improved for DECALS, and ported back to RUSSEL. 

## <a name="3_0"></a>Instructions for Setting up DECALS, and the Competency System UI

These are instructions for getting the RUSSEL, DECALS, and Competency System installed and running on a VMware VM running Ubuntu Linux version 14.04.3. These instructions can also be used to setup the system software on a server running Ubuntu Linux version 14.04.3.

#### <a name="3_0_1"></a>Code projects in this Github repository required to build DECALS:
+ decals-ui
+ EwGWTLib
+ competency-ui
+ LEVR
    * eduworks-common
    * levr-core
    * levr-base
+ scripts
    * base-v2
    * base
    * competency
    * decals


#### <a name="3_0_2"></a>Prerequisites:
+ VMWare Player 7.1.2 or higher OR
+ A web server running Linux
+ Ubuntu Linux 14.04.3 64 bit
+ Java 7
+ Tomcat 7
+ Solr
+ Eclipse Kepler v4.3 for Java EE Developers 64-bit and the GWT plugin


[Download and Install VMware Player 7.1.2](https://my.vmware.com/web/vmware/free#desktop_end_user_computing/vmware_player/7_0 "VMware Player 7.1.1")

#### <a name="3_1"></a>VMware VM settings:
+ CPUs: 2
+ RAM: 4GB
+ Vitualization Engine: Intel VT-x/EPT or AMD V/RVI with Virtualize Intel VT-x/EPT or AMD V/RVI box checked
+ VM OS: Ubuntu 14.04.3


### <a name="3_2"></a>Install Ubuntu OS
[Download Ubuntu 14.04.3 and install in the new VM](http://www.ubuntu.com/download/desktop "Ubuntu 14.04.3")

[Ubuntu 14.04.3 LTS 64-bit installation in VMware player 2015](https://www.youtube.com/watch?v=PZFyhzUcwjA "YouTube how-to video")

### <a name="3_3"></a>Install Eclipse Development Environment

[Download and install Eclipse Kepler v4.3 for Java EE Developers 64-bit on VM](http://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/kepler/SR2/eclipse-jee-kepler-SR2-linux-gtk-x86_64.tar.gz "Eclipse Kepler v4.3 for Java EE Developers")

[Install GWT Eclipse Plugin](https://developers.google.com/eclipse/docs/install-eclipse-4.3 "GWT Eclipse Plugin")

### <a name="3_4"></a>Install Java 7:
```
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java7-installer
sudo apt-get install oracle-java7-set-default
```

### <a name="3_5"></a>Install Java JDK:
```
sudo apt-get install default-jdk
```

### <a name="3_6"></a>Install Apache Ant/Git:
```
sudo apt-get install ant git
```

### <a name="3_7"></a>Install Apache Tomcat 7:
```
sudo apt-get install tomcat7
```

### <a name="3_8"></a>Install Apache Solr DBs:

[Download Solr](http://mirror.metrocast.net/apache/lucene/solr/4.10.0 "Solr 4.10.0")

**Untar file:**
```
tar -zxvf solr-4.10.0.tgz
```

**Copy contents of folder /solr-4.10.0/example/lib/ext/ to /usr/share/tomcat7/lib/**
```
cp -r ~/solr-4.10.0/example/lib/ext/*.jar /usr/share/tomcat7/lib/
```

**Edit file "tomcat-users.xml" in directory /var/lib/tomcat7/conf/ and setup these users:**
```xml
<tomcat-users>
  <role rolename="manager"/>
  <role rolename="manager-gui"/>
  <role rolename="admin"/>
  <role rolename="admin-gui"/>
  <user username="tomcat" password="tomcat" roles="manager,manager-gui, admin, admin-gui"/>
</tomcat-users> 
```

#### <a name="3_8_1"></a>Setup Apache Solr DB for DECALS DB:


**Copy contents of folder /solr-4.10.0/example/solr/ to /var/lib/tomcat7/darSolr**

```
sudo mkdir /var/lib/tomcat7/darSolr/
cp -r ~/solr-4.x.x/example/solr/ /var/lib/tomcat7/darSolr/ 
```

**Copy /solr-4.10.0/dist/solr-4.10.0.war to /var/lib/tomcat7/darSolr/darSolr.war**
```
cp ~/solr-4.10.0/dist/solr-4.10.0.war /var/lib/tomcat7/darSolr/darSolr.war
```

**set permissions on darSolr folder:**
```
sudo chmod -R 755 /var/lib/tomcat7/darSolr/
```

**Create file "darSolr.xml" in directory /var/lib/tomcat7/conf/Catalina/localhost/ with the following contents:**
```
cd /var/lib/tomcat7/conf/Catalina/localhost/
sudo vim darSolr.xml
```
```xml
<?xml version="1.0" encoding="utf-8"?>
<Context docBase="/var/lib/tomcat7/darSolr/darSolr.war" debug="0" crossContext="true">
<Environment name="solr/home" type="java.lang.String" value="/var/lib/tomcat7/darSolr" override="true" />
</Context>
```

#### <a name="3_8_2"></a>Setup Apache Solr DB for LR Registry DB:


**Copy contents of folder /solr-4.10.0/example/solr/ to /var/lib/tomcat7/registrySolr**

```
sudo mkdir /var/lib/tomcat7/registrySolr/
cp -r ~/solr-4.x.x/example/solr/ /var/lib/tomcat7/registrySolr/ 
```

**Copy /solr-4.10.0/dist/solr-4.10.0.war to /var/lib/tomcat7/registrySolr/registrySolr.war**
```
cp ~/solr-4.10.0/dist/solr-4.10.0.war /var/lib/tomcat7/registrySolr/registrySolr.war
```

**set permissions on registrySolr folder:**
```
sudo chmod -R 755 /var/lib/tomcat7/registrySolr/
```

**Create file "registrySolr.xml" in directory /var/lib/tomcat7/conf/Catalina/localhost/ with the following contents:**
```
cd /var/lib/tomcat7/conf/Catalina/localhost/
sudo vim registrySolr.xml
```
```xml
<?xml version="1.0" encoding="utf-8"?>
<Context docBase="/var/lib/tomcat7/registrySolr/registrySolr.war" debug="0" crossContext="true">
<Environment name="solr/home" type="java.lang.String" value="/var/lib/tomcat7/registrySolr" override="true" />
</Context>
```

### <a name="3_9"></a>Download the source code from this repository:

Create a “Source/” directory to hold the master repositories from Github and create a “Development/” directory to build the code.
```
sudo mkdir Source
sudo mkdir Development
```

**To download the repositories from Github:**

```
cd Source
sudo git clone <DECALS Github repo URL>  
```

**Once the files have finished downloading copy them over to your Development/ folder:**
```
sudo cp -R * ~/Development/
```

### <a name="3_10"></a>Setting up and building the decals-ui source project

### <a name="3_11"></a>Setting up the decals-ui and EwGWTLib projects

Run the Eclipse IDE
Once Eclipse has finished loading, 
+ go to  **File->Import**
+ From the Import dialog, select **General->Existing Projects into Workspace** and click Next>
+ Browse to ~/Development/decals-ui/ and click Finish

This should place the project decals-ui in the Project Explorer.
Next, from the Import dialog, 
+ select **General->Existing Projects into Workspace** and click Next>
+ Browse to ~/Development/EwGWTLib/ and click Finish

This should place the project EwGWTLib in the Project Explorer.

+ Right-click on the decals-ui project and select properties
+ From the properties dialog select Java Build Path.
+ Under the Libraries tab, make sure the GWT SDK is set. If not, click edit and select Use specific SDK: GWT 2.6.0 or higher.
+ Right-click on the EwGWTLib project and select properties
+ From the properties dialog select Java Build Path.
+ Under the Libraries tab, make sure the GWT SDK is set. If not, click edit and select Use specific SDK: GWT 2.6.0 or higher.
+ If the GWT SDK library is not in the list, make sure that the following jar files point to plugins\com.google.gwt.eclipse.sdkbundle_2.6.0\gwt-2.6.0:
⋅⋅* gwt-dev.jar
⋅⋅* gwt-user.jar
⋅⋅* validation-api-1.0.0.GA.jar
⋅⋅* validation-api-1.0.0.GA-sources.jar

#### <a name="3_11_1"></a>To build the decals-ui project:
+ Right-click on the decals-ui project and select Google->GWT Compile
+ From the dialog choose the output style of choice and click compile.


### <a name="3_12"></a>Building the LEVR components (levr.war file)

**Some changes need to be made in the original build.xml files:**

+ in build.xml for eduworks-common, change **"eduworks-common-usage"** on top line to **"eduworks-common-jar"**
+ in build.xml for levr-core change **"levr-core-dist"** on top line to **"levr-core-jar"**

The projects: eduworks-common, levr-core and levr-base must be built in the following order:
1. eduworks-common
2. levr-core 
3. levr-base

**To build each project, from the Linux commandline run:**
```
cd ~Development/LEVR/eduworks-common/
sudo ant 
cd ~Development/LEVR/levr-core/
sudo ant 
cd ~Development/LEVR/levr-base/
sudo ant
```

Once each project has been built, copy the file, **levr.war**, from the Development/LEVR/levr-base/dist folder to /var/lib/tomcat7/webapps/ directory
```
sudo cp levr.war /var/lib/tomcat7/webapps/
```

**NOTE**: After the initial LEVR build and before copying any newly built levr.war file over, it is a good idea to stop the tomcat7 service and remove the old levr.war file and associated levr directory from the /var/lib/tomcat7/webapps/ directory:
```
sudo service tomcat7 stop (wait to start service again once all projects are setup and configured – see below)
cd /var/lib/tomcat7/webapps/
sudo rm –R lev*
```

### <a name="3_13"></a>Installing LEVR scripts

**Create a directory, etc/ under /var/lib/tomcat7/:**
```
sudo mkdir etc/
```

**Copy *.rs2 script files from ~/Source/scripts/base-v2/, ~/Source/scripts/base/, ~/Source/scripts/competency/, and ~/Source/scripts/decals/ to /var/lib/tomcat7/etc directory:**

```
cd ~/Source/scripts/base-v2/
sudo cp * /var/lib/tomcat7/etc/

cd ~/Source/scripts/base/
sudo cp * /var/lib/tomcat7/etc/

cd ~/Source/scripts/competency/
sudo cp * /var/lib/tomcat7/etc/

cd ~/Source/scripts/decals/
sudo cp * /var/lib/tomcat7/etc/
```
**make the owner of etc/ and all sub files and folders tomcat7:**
```
sudo chown –R tomcat7:root etc/
```

### <a name="3_14"></a>Creating a location for the database files

**Create a directory, db/ under /var/lib/tomcat7/:**
```
sudo mkdir db/
```
**make the owner of db/ and all sub files and folders tomcat7:**
```
sudo chown –R tomcat7:root db/
```

### <a name="3_15"></a>Install and configure the decals-ui project

**Create a decals/ directory under webapps/ROOT/:**
```
sudo mkdir /var/lib/tomcat7/webapps/ROOT/decals/
```
**Copy the contents of the ~/Development/decals-ui/war/ to /var/lib/tomcat7/webapps/ROOT/decals/:**
```
cd ~/Development/decals-ui/war/
sudo cp –R * /var/lib/tomcat7/webapps/ROOT/decals/
```
#### <a name="3_15_1"></a>Open decals/js/installation.settings for edit:
```
cd /var/lib/tomcat7/webapps/ROOT/decals/js/
sudo vim installation.settings
```
**File contents should be:**
```
site.name="DECALS"

root.url="http://<server url>"
esb.url="http://<server url>/levr/api/custom/"

alfresco.url="N/A"
site.url="http://<server url>/decals/"
help.url="N/A"
feedback.email="N/A"

competency.url="http://<server url>/competency/"

int.search.thumbnail.root="http://<server url>/thumb/thumbnails/"
```

save the file.

#### <a name="3_15_2"></a>Open /var/lib/tomcat7/etc/decals.competency.config.rs2 for edit:
The following lines should be edited for your local server environment:

line 12:
```
defaultURIPrefix = #string(str="http://<server uri>/competency/");
```

line 13:
```
comment out this line by adding //
```

line 36:
```
defaultDirectory = #add(b="etc/competency/");
```

line 45:
```
structureDirectory = #add(b="etc/structure-files/");
```

line 54:
```
backupRestoreKeyPath = #string(str="etc/competency.backupRestore.key/");
```

line 55:
```
comment out this line by adding //
```

save the file.

#### <a name="3_15_3"></a>Open /var/lib/tomcat7/etc/decals.config.rs2 for edit:
The following lines should be edited for your local server environment:

line 4:
```
initialAdminPassword = #add(a="<whatever you want as password>");
```

line 9:
```
fileDownloadUrlPrefix  = #string(str="http://<server uri>/levr/api/custom/decalsFileDownload?fileId=");
```

line 14/15:
```
lrSearchApiKey = #string(str="<insert key here>");
comment out this line by adding //
```

line 17/18:
```
mashapeKey= #string(str="<insert key here>");
comment out this line by adding //
```

line 23/24:
```
darSolrUrl = #string(str="http://<server uri>/darSolr/");
registrySolrUrl = #string(str="http://<server uri>/registrySolr/");
```

line 30:
```
fileDownloadUrlPrefix  = #string(str="http://service.metaglance.com/metadataLite/russel/generate");
```

line 72:
```
defaultDirectory = #string(str="./db/");
```

line 79:
```
rootDbDirectory = #string(str="db/");
```

save the file.

### <a name="3_16"></a>Installing the competency-ui source

Create a competency directory under webapps/ROOT/:
```
sudo mkdir /var/lib/tomcat7/webapps/ROOT/competency/
```

Copy the competency-ui files into the new directory:
```
cd ~/Source/competency-ui/
sudo cp –R * /var/lib/tomcat7/webapps/ROOT/competency/
```
Move the WEB-INF directory up a level to ROOT/:
```
cd /var/lib/tomcat7/webapps/ROOT/competency/
sudo mv WEB-INF ../
```

#### <a name="3_16_1"></a>Open /var/lib/tomcat7/webapps/ROOT/competency/index.html for edit: 
The following lines should be edited for your local server environment:

line 9:
```
change <base href="/"> to <base href="/competency/">
```

#### <a name="3_16_2"></a>Open /var/lib/tomcat7/webapps/ROOT/competency/js/definitions.js for edit: 
The following lines should be edited for your local server environment:

line 4:
```
change: value('apiURL', 'http://localhost:9722/api/custom/competency/')
to: value('apiURL', 'http://<your domain>:<your port>/levr/api/custom/competency/')
```
### <a name="3_17"></a>Final Steps

Make sure the following files, directories and subdirectories are set to ownership tomcat7:root:
+ /var/lib/tomcat7/etc/
+ /var/lib/tomcat7/db/
+ /var/lib/tomcat7/darSolr/
+ /var/lib/tomcat7/registrySolr/
+ /var/lib/tomcat7/webapps/levr.war
+ /var/lib/tomcat7/webapps/ROOT/decals/
+ /var/lib/tomcat7/webapps/ROOT/competency/

```
sudo chown -R tomcat7:root <file or directory>
```
To start the tomcat7 service:
```
sudo service tomcat7 start
```

Navigate to http://<server url>/decals/ to visit DECALS website
Navigate to http://<server url>/darSolr/ to check darSolr DB stats
Navigate to http://<server url>/registrySolr/ to check registrySolr DB stats


## Contributing to the project <a name="CTTP"></a>
We welcome contributions to this project. Fork this repository, make changes, and submit pull requests. If you're not comfortable with editing the code, please [submit an issue](https://github.com/adlnet/DECALS/issues) and we'll be happy to address it. 

## License <a name="License"></a>
   Copyright &copy;2016 Advanced Distributed Learning

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
