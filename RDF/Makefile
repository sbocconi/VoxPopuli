include ../mkfile

RDF_FS = IWA.rdf
REP = IWA

RDFS_FS = VoxPopuli.rdfs

RDFS_TGFS = ${RDFS_FS:%.rdfs=$(RDFSDESTDIR)/%.rdfs}
RDF_TGFS = ${RDF_FS:%.rdf=$(RDFDESTDIR)/%.rdf}
ORFILES = $(RDFS_FS) $(RDF_FS)
TARGETFILES = $(RDFS_TGFS) $(RDF_TGFS)

BASEDIR = /ufs/media/public_html/ns

RDFSDESTDIR =$(BASEDIR)/VP
RDFDESTDIR =$(BASEDIR)/$(REP)

ARIESBASEDIR = /net/aries/export/data2/media/video/IWA/ns

RDFARIESBASEDIR = $(ARIESBASEDIR)/$(REP)
RDFSARIESBASEDIR = $(ARIESBASEDIR)/VP

TCDIR = /net/helenos/export/scratch1/sbocconi/tomcat
OFFTCDIR = /net/aries/export/data1/tomcats/demo
#ADMINUSER = jrvosse
ADMINUSER = sbocconi


# Handy formula to keep here for future reference
#define Rule
#$(1):
#$$(MAKE) RDF=$(1).rdf  fix_namespace
#$$(MAKE) RDF=$(1).rdfs fix_namespace
#$$(MAKE) RDF=$$(PWD)/$(1).rdf.fixed  REP=IWA upload_rep
#$$(MAKE) RDF=$$(PWD)/$(1).rdfs.fixed REP=IWA upload_rep
#cp $(1).rdf.fixed $$(DESTDIR)/$(1).rdf
#cp $(1).rdfs.fixed $$(DESTDIR)/$(1).rdfs
#rm -f $(1).rdf.fixed $(1).rdfs.fixed
#endef

#$(foreach file,$(FILES),$(eval $(call Rule,$(file))))


$(RDFSDESTDIR)/%.rdfs: %.rdfs
	$(MAKE) RDF=$<  fix_namespace
	mv $<.fixed $(RDFSDESTDIR)/$<
	$(MAKE) RDF=$(RDFSDESTDIR)/$<  REP=$(REP) upload_rep

$(RDFDESTDIR)/%.rdf: %.rdf
	$(MAKE) RDF=$<  fix_namespace
	mv $<.fixed $(RDFDESTDIR)/$<
	$(MAKE) RDF=$(RDFDESTDIR)/$<  REP=$(REP) upload_rep

%.rdf:
	@echo $%
clean:
	rm -rf *.fixed *.tmp
	
clear:	
	rm -f $(TARGETFILES)
	$(MAKE) REP=$(REP)   clear_rep

upload: clear $(TARGETFILES)
	chmod 664 $(RDFSDESTDIR)/*.rdfs
	chmod 664 $(RDFDESTDIR)/*.rdf
	$(TCDIR)/tomcat stop
	$(TCDIR)/tomcat start
	@echo tomcat restarted


offupload: clear $(TARGETFILES)
	chmod 664 $(RDFSDESTDIR)/*.rdfs
	chmod 664 $(RDFDESTDIR)/*.rdf
	cp $(RDFSDESTDIR)/*.rdfs $(RDFSARIESBASEDIR)/
	cp $(RDFDESTDIR)/*.rdf $(RDFARIESBASEDIR)/
	(($(OFFTCDIR)/tomcat stop && $(OFFTCDIR)/tomcat start && echo -e "\nOFF tomcat restarted") || (echo -e "$(OFFTCDIR)/tomcat stop && $(OFFTCDIR)/tomcat start" | mail $(ADMINUSER) -s "Please execute the following" &&  echo -e "\nMail to restart sent to $(ADMINUSER)"))
	
print:
	@echo $(RDF_FS) $(RDFS_FS) $(TARGETFILES) $(ORFILES)

url:
	$(MAKE) REP=$(REP) RDF=url.rdf upload_rep
