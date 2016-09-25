MAINSRCDIR = ./src/
SRCDIR = ./src/voxpopuli/
CLASSESDIR = ./build/classes/voxpopuli/
MAINCLASSESDIR = ./build/classes/
SESAMEJAR = '/cygdrive/c/Documents and Settings/sbocconi/Documenti/Lavoro/Software/Libraries/sesame-1.2.6/lib/sesame.jar'
MODELJAR = '/cygdrive/c/Documents and Settings/sbocconi/Documenti/Lavoro/Software/Libraries/sesame-1.2.6/lib/openrdf-model.jar'
UTILJAR = '/cygdrive/c/Documents and Settings/sbocconi/Documenti/Lavoro/Software/Libraries/sesame-1.2.6/lib/openrdf-util.jar'
RIOJAR = '/cygdrive/c/Documents and Settings/sbocconi/Documenti/Lavoro/Software/Libraries/sesame-1.2.6/lib/rio.jar'
BASEDIR = '/cygdrive/c/Documents and Settings/sbocconi/Documenti/Lavoro/Software/VP/SMIL/'
REALFILE =${BASEDIR}/LinuxGenero
VIDEOLOC ='/cygdrive/c/Documents and Settings/sbocconi/Documenti/Lavoro/Software/VP/video/'
LOCAL = true
TCBASE = '/cygdrive/c/Documents and Settings/sbocconi/.netbeans/5.5/apache-tomcat-5.5.17_base/'
TCHOME = '/cygdrive/c/Programmi/netbeans-5.5/enterprise3/apache-tomcat-5.5.17/'

VPDIR = '/cygdrive/c/Documents and Settings/sbocconi/Documenti/Lavoro/Software/VP/'
VPTARGETDIR = '/cygdrive/c/Documents and Settings/sbocconi/.netbeans/5.5/apache-tomcat-5.5.17_base/webapps/cocoon/VP/'

RDFDIR = '/cygdrive/c/Documents and Settings/sbocconi/Documenti/Lavoro/Software/RDF/'
RDFTARGETDIR = '/cygdrive/c/Documents and Settings/sbocconi/.netbeans/5.5/apache-tomcat-5.5.17_base/webapps/cocoon/VP/ns/'

JARFILE = VoxPopuli.jar
LOCALJARDIR = ./dist/
JARDIR = $(TCBASE)webapps/cocoon/WEB-INF/lib/



#ADMINUSER = jrvosse
ADMINUSER = sbocconi

NAMESPACE=http://www.cwi.nl/~media/ns/IWA/IWA.rdf#
SESAMESERVER=http://localhost:8084/sesame/

JAVA_HOME = '/cygdrive/c/Programmi/Java/jdk1.6.0/'
JAVAC	= $(JAVA_HOME)bin/javac
JAVA	= $(JAVA_HOME)bin/java -Xmx1024m
JAR     = $(JAVA_HOME)bin/jar

LOCALPROGRAM = $(JAVA) $(MAIN) true $(RDFDIR) IWA $(NAMESPACE) $(VIDEOLOC)
REMOTEPROGRAM = $(JAVA) $(MAIN) false $(SESAMESERVER) IWA $(NAMESPACE) $(VIDEOLOC)

# Check performances depending on iterations
ITER = 3
ACT0 = 0
ACT1 = 1
ACT2 = 2
ACT3 = 3 

MINTIME = 3
MAXTIME = 60



SRC	= $(wildcard $(SRCDIR)*.java)
CLASSES = ${SRC:$(SRCDIR)%.java=$(CLASSESDIR)%.class}
LIBS	=
MAIN	= $(MAINSRCDIR)VPengine
.SUFFIXES: .java .class



CLASSPATH=$(CLASSESDIR):$(SESAMEJAR):$(MODELJAR):$(UTILJAR):$(RIOJAR):

#/soft/JBuilder-9/lib/xercesImpl.jar:/soft/JBuilder-9/lib/xmlParserAPIs.jar:/soft/JBuilder-9/jdk1.4/demo/jfc/Java2D/Java2Demo.jar:/soft/JBuilder-9/jdk1.4/demo/plugin/jfc/Java2D/Java2Demo.jar:/soft/JBuilder-9/jdk1.4/jre/lib/ext/sunjce_provider.jar:/soft/JBuilder-9/jdk1.4/jre/lib/ext/dnsns.jar:/soft/JBuilder-9/jdk1.4/jre/lib/ext/localedata.jar:/soft/JBuilder-9/jdk1.4/jre/lib/ext/ldapsec.jar:/soft/JBuilder-9/jdk1.4/jre/lib/sunrsasign.jar:/soft/JBuilder-9/jdk1.4/jre/lib/jce.jar:/soft/JBuilder-9/jdk1.4/jre/lib/jsse.jar:/soft/JBuilder-9/jdk1.4/jre/lib/charsets.jar:/soft/JBuilder-9/jdk1.4/jre/lib/im/indicim.jar:/soft/JBuilder-9/jdk1.4/jre/lib/javaplugin.jar:/soft/JBuilder-9/jdk1.4/jre/lib/rt.jar:/soft/JBuilder-9/jdk1.4/lib/htmlconverter.jar:/soft/JBuilder-9/jdk1.4/lib/tools.jar:/soft/JBuilder-9/jdk1.4/lib/dt.jar:/soft/mysql-connector-java-3.0.10-stable/mysql-connector-java-3.0.10-stable-bin.jar

default: compile

compile : $(CLASSES) $(MAINCLASSESDIR)$(MAIN).class
#compile : $(CLASSES)
	@echo "Compiling done."

usage:
	@echo -e "make [run|compile]\n"

debug:
	@echo JAVA $(JAVA)
	@echo CLASSES $(CLASSES)
	@echo CLASSPATH ${CLASSPATH}

clean:
	rm -rf $(CLASSESDIR)*.class $(CLASSESDIR)$(PACKAGE)*.class ./*~ $(LOCALJARDIR)$(JARFILE)

$(CLASSESDIR)%.class: $(SRCDIR)%.java
	@echo "Compiling $<"
	CLASSPATH=${CLASSPATH} $(JAVAC) -d . $<

#%.class: %.java
#	@echo "Compiling $<"
#	CLASSPATH=${CLASSPATH} $(JAVAC) -d . $<

install:

	@echo -e "Not implemented yet\n"


LLongestPath: compile
	CLASSPATH=${CLASSPATH}  $(LOCALPROGRAM) L $(REALFILE)

RLongestPath:: compile
	CLASSPATH=${CLASSPATH}  $(REMOTEPROGRAM) L $(REALFILE)

LMultiplevoices: compile
	CLASSPATH=${CLASSPATH}  $(LOCALPROGRAM) M $(REALFILE) O attack K N

RMultiplevoices: compilevoxpopuli
	CLASSPATH=${CLASSPATH}  $(REMOTEPROGRAM) M $(REALFILE) O attack K N

LAll: compile
	CLASSPATH=${CLASSPATH}  $(LOCALPROGRAM) A $(REALFILE) O

RAll: compile
	CLASSPATH=${CLASSPATH}  $(REMOTEPROGRAM) A $(REALFILE) O

LCheckAss: compile
	CLASSPATH=${CLASSPATH}  $(LOCALPROGRAM) C A

RCheckAss: compile
	CLASSPATH=${CLASSPATH}  $(REMOTEPROGRAM) C A

LCheckNoAss: compile
	CLASSPATH=${CLASSPATH}  $(LOCALPROGRAM) C NA

RCheckNoAss: compile
	CLASSPATH=${CLASSPATH}  $(REMOTEPROGRAM) C NA

Suggest: compile
	CLASSPATH=${CLASSPATH}  $(LOCALPROGRAM) S
	
CheckIter: compile
	CLASSPATH=${CLASSPATH}  $(LOCALPROGRAM) R $(ITER) >/dev/null

CheckRetr: compile
	CLASSPATH=${CLASSPATH}  $(LOCALPROGRAM) R $(ITER)
	
Video: compile
	CLASSPATH=${CLASSPATH}  $(LOCALPROGRAM) V $(MINTIME) $(MAXTIME)
	
Editor: compile
	CLASSPATH=${CLASSPATH}  $(JAVA) VPEditor true $(RDFDIR) IWA $(NAMESPACE) $(VIDEOLOC) 100 WebTestIWA $(REALFILE)

jar:  $(JARDIR)$(JARFILE)
	@echo JAR OK

$(JARDIR)$(JARFILE):  $(LOCALJARDIR)$(JARFILE)
	cp $(SESAMEJAR) $(MODELJAR) $(UTILJAR) $(RIOJAR) $(JARDIR)
	cp $(LOCALJARDIR)$(JARFILE) $(JARDIR)
	$(JAR) -tvf $(JARDIR)$(JARFILE)
	$(TCHOME)bin/shutdown.bat
	$(TCHOME)bin/startup.bat
	@echo JAR made and tomcat restarted


$(LOCALJARDIR)$(JARFILE):  $(CLASSES)
	$(JAR) -cvf $(LOCALJARDIR)$(JARFILE) $(CLASSESDIR)*.class




tomcat:
	set CATALINA_HOME=$(TCHOME) $(TCHOME)bin/shutdown.bat
	set CATALINA_HOME=$(TCHOME) $(TCHOME)bin/startup.bat
	@echo tomcat restarted

csv:
	cat a.csv | sed 's/\./,/g' > b.csv
	
web:
	cp $(VPDIR)*.* $(VPTARGETDIR)
	cp $(RDFDIR)IWA.rdf $(RDFTARGETDIR)IWA/
	cp $(RDFDIR)VJ.rdf $(RDFTARGETDIR)VJ/
	cp $(RDFDIR)VoxPopuli.rdfs $(RDFTARGETDIR)VP/
