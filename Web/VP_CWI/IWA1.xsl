<?xml version="1.0"?>
<!DOCTYPE xsl:stylesheet [
]>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


  <!-- VARIABLES -->

  <!-- General variables  -->

  <xsl:variable name="curlang"><xsl:value-of select="/page/selected_values/curlang"/></xsl:variable>
  <xsl:variable name="DomainNS"><xsl:value-of select="/page/selected_values/domainNS"/></xsl:variable>
  <xsl:variable name="Repository"><xsl:value-of select="/page/selected_values/repository"/></xsl:variable>
  <xsl:variable name="RDFLocation"><xsl:value-of select="/page/selected_values/RDFLocation"/></xsl:variable>
  <xsl:variable name="Local"><xsl:value-of select="/page/selected_values/local"/></xsl:variable>
  <xsl:variable name="VideoLocation"><xsl:value-of select="/page/selected_values/videolocation"/></xsl:variable>
  <xsl:variable name="StillLocation"><xsl:value-of select="/page/selected_values/stilllocation"/></xsl:variable>
  <xsl:variable name="Quality"><xsl:value-of select="/page/selected_values/quality"/></xsl:variable>
  <xsl:variable name="Caption"><xsl:value-of select="/page/selected_values/caption"/></xsl:variable>



  <!-- IWA specific variables  -->
  <xsl:variable name="LabelSubmit"><xsl:value-of select="/page/labelsubmit"/></xsl:variable>
  <xsl:variable name="Strategy"><xsl:value-of select="/page/selected_values/strategy"/></xsl:variable>
  <xsl:variable name="Stage"><xsl:value-of select="/page/selected_values/stage"/></xsl:variable>
  <xsl:variable name="Intercut"><xsl:value-of select="/page/selected_values/intercut"/></xsl:variable>

  <!-- TEMPLATES -->

  <!-- generate headers for table -->
  <xsl:template match="SocClass" mode="header">
    <xsl:param name="Number" />
    <th>
      <xsl:value-of select="./label"/>
    </th>
    <xsl:apply-templates select="SocClass" mode="header"/>
  </xsl:template>

  <xsl:template match="SocClass" mode="body">
    <xsl:param name="Number" />
    <td>
      <select size="6">
        <xsl:attribute name="multiple">multiple</xsl:attribute>
        <!--<select size="8" onChange="this.form.submit();">-->
          <xsl:variable name="Label"><xsl:value-of select="./label"/></xsl:variable>
          <xsl:attribute name="name">
            <xsl:value-of select="concat(normalize-space($Label),$Number)"/>
          </xsl:attribute>
          <xsl:apply-templates select="SocSubClassList/SocSubClass" >
            <xsl:with-param name="Number" >
              <xsl:value-of select="$Number" />
            </xsl:with-param>
          </xsl:apply-templates >
        </select>
      </td>
      <xsl:apply-templates select="SocClass" mode="body"/>
    </xsl:template>

    <!-- generate list of elements for drop-down menu -->
    <xsl:template match="SocSubClass">
      <xsl:param name="Number" />
      <xsl:variable name="Id"><xsl:value-of select="./id"/></xsl:variable>
      <option>
        <xsl:variable name="selected"><xsl:value-of select="./selected"/></xsl:variable>
        <xsl:if test="$selected=$Number">
          <xsl:attribute name="selected">selected</xsl:attribute>
        </xsl:if>
        <xsl:attribute name="value">
          <xsl:value-of select="normalize-space($Id)"/>
        </xsl:attribute>
        <xsl:value-of select="./label"/>
      </option>
      <xsl:apply-templates select="SocSubClass" />
    </xsl:template>


    <!-- generate list of elements for drop-down menu -->
    <xsl:template match="Concept">
      <xsl:param name="Number" />
      <xsl:variable name="Id"><xsl:value-of select="./id"/></xsl:variable>
      <option>
        <xsl:variable name="selected"><xsl:value-of select="./selected"/></xsl:variable>
        <xsl:if test="$selected=$Number">
          <xsl:attribute name="selected">selected</xsl:attribute>
        </xsl:if>
        <xsl:attribute name="value">
          <xsl:value-of select="normalize-space($Id)"/>
        </xsl:attribute>
        <xsl:value-of select="./label"/>
      </option>
      <xsl:apply-templates select="Concept" />
    </xsl:template>

    <!-- generate list of elements for drop-down menu -->
    <xsl:template match="Question">
      <xsl:param name="Number" />
      <xsl:variable name="Id"><xsl:value-of select="./id"/></xsl:variable>
      <option>
        <xsl:variable name="selected"><xsl:value-of select="./selected"/></xsl:variable>
        <xsl:if test="$selected=$Number">
          <xsl:attribute name="selected">selected</xsl:attribute>
        </xsl:if>
        <xsl:attribute name="value">
          <xsl:value-of select="normalize-space($Id)"/>
        </xsl:attribute>
        <xsl:value-of select="./label"/>
      </option>
      <xsl:apply-templates select="Question" />
    </xsl:template>

    <!-- generate list of elements for drop-down menu -->
    <xsl:template match="Interviewee">
      <xsl:param name="Number" />
      <xsl:variable name="Id"><xsl:value-of select="./id"/></xsl:variable>
      <option>
        <xsl:variable name="selected"><xsl:value-of select="./selected"/></xsl:variable>
        <xsl:if test="$selected=$Number">
          <xsl:attribute name="selected">selected</xsl:attribute>
        </xsl:if>
        <xsl:attribute name="value">
          <xsl:value-of select="normalize-space($Id)"/>
        </xsl:attribute>
        <xsl:value-of select="./label"/>
      </option>
      <xsl:apply-templates select="Interviewee" />
    </xsl:template>

    <!-- generate list of elements for drop-down menu -->
    <xsl:template match="Opinion">
      <xsl:param name="Number" />
      <xsl:variable name="Id"><xsl:value-of select="./id"/></xsl:variable>
      <option>
        <xsl:variable name="selected"><xsl:value-of select="./selected"/></xsl:variable>
        <xsl:if test="$selected=$Number">
          <xsl:attribute name="selected">selected</xsl:attribute>
        </xsl:if>
        <xsl:attribute name="value">
          <xsl:value-of select="normalize-space($Id)"/>
        </xsl:attribute>
        <xsl:value-of select="./label"/>
      </option>
      <xsl:apply-templates select="Opinion" />
    </xsl:template>


    <!-- generate quality input for form -->
    <xsl:template name="qualityinput">
      <td>
        <input type="radio" name="quality" value="_20">
          <!--<xsl:attribute name="onClick">this.form.submit();</xsl:attribute> -->
          <xsl:if test="$Quality = '_20'">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
          Low Bandwidth
        </input>
      </td>
      <tr>
        <td>
          <input type="radio" name="quality" value="_100">
            <!--<xsl:attribute name="onClick">this.form.submit();</xsl:attribute> -->
            <xsl:if test="$Quality = '_100'">
              <xsl:attribute name="checked">checked</xsl:attribute>
            </xsl:if>
            Medium Bandwidth
          </input>
        </td>
      </tr>
      <tr>
        <td>
          <input type="radio" name="quality" value="_225">
            <!--<xsl:attribute name="onClick">this.form.submit();</xsl:attribute> -->
            <xsl:if test="$Quality = '_225'">
              <xsl:attribute name="checked">checked</xsl:attribute>
            </xsl:if>
            High Bandwidth
          </input>
        </td>
      </tr>
    </xsl:template>



    <xsl:template name="hiddeninput">

      <input type="hidden" name="curlang">
        <xsl:attribute name="value">
          <xsl:value-of select="$curlang"/>
        </xsl:attribute>
      </input>

      <input type="hidden" name="stillLocation">
        <xsl:attribute name="value">
          <xsl:value-of select="$StillLocation"/>
        </xsl:attribute>
      </input>

      <input type="hidden" name="RDFLocation">
        <xsl:attribute name="value">
          <xsl:value-of select="$RDFLocation"/>
        </xsl:attribute>
      </input>
      <input type="hidden" name="repository">
        <xsl:attribute name="value">
          <xsl:value-of select="$Repository"/>
        </xsl:attribute>
      </input>
      <input type="hidden" name="domainNS">
        <xsl:attribute name="value">
          <xsl:value-of select="$DomainNS"/>
        </xsl:attribute>
      </input>
      <input type="hidden" name="local">
        <xsl:attribute name="value">
          <xsl:value-of select="$Local"/>
        </xsl:attribute>
      </input>
      <input type="hidden" name="videolocation">
        <xsl:attribute name="value">
          <xsl:value-of select="$VideoLocation"/>
        </xsl:attribute>
      </input>
    </xsl:template>

    <!-- generate body of the HTML page -->
    <xsl:template match="page">
      <xsl:if test="$Stage = 'one'">
        <html>
          <head>
<!--              <style type="text/css">
                body { 
			background:#CCCCCC;
			background:url("../VP/DSCN0384.jpg") repeat-y;
                      }
		.colorlabel { color: #F63E0C; }
              </style>
-->
            <link
              type="text/css"
              rel="stylesheet"
              href="../VP/GlobalFormatting.css" />

            <link 
              type="text/css"
              rel="stylesheet"
              href="../VP/GlobalLayout.css" />

              <title>VOX POPULI DEMO</title>
            </head>

            <body id="header">

<div id="LeftBar">

	        <p><i>To play this demo, you need to have <a
	        href="http://www.real.com/player/">RealPlayer</a>
	        installed. If it is installed on your system,
	       	you should be able to see this <a
	       	href="http://homepages.cwi.nl/~media/demo/VoxPopuli/EditExample.rm">video</a></i>.</p>

                Vox Populi automatically generates short documentaries about matter-of-opinion issues. To make using this demo easier, 
                we provide some preselected combinations so that you can get an idea of its possibility (<i>please note that your first selection might take 90 seconds to get a documentary, afterwards the documentaries are generated immediately</i>):

<ul>
                
                <li>A lawyer in Harvard talks about the war in Afghanistan, and she is in favor of the military intervention. 
                Afro-American people seem to have a different opinion 
                (click <a href="http://media.cwi.nl/cocoon/cuypers/IWA/index.html?stage=two&amp;amp;curlang=44&amp;amp;stillLocation=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fdemo%2FVoxPopuli%2F&amp;RDFLocation=%2Fexport%2Fdata2%2Fmedia%2Fvideo%2FIWA%2Fns%2F&amp;repository=IWA&amp;domainNS=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FIWA%2FIWA.rdf%23&amp;local=true&amp;videolocation=rtsp%3A%2F%2Fmedia.cwi.nl%2Fmedia%2Fvideo%2FIWA%2Freal%2F&amp;Interviewees=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FIWA%2FIWA.rdf%23IWA_00218&amp;Opinions=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FVP%2FVoxPopuli.rdfs%23IWA_Instance_8&amp;Gender2=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FVP%2FVoxPopuli.rdfs%23Male&amp;Race2=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FVP%2FVoxPopuli.rdfs%23Black&amp;strategy=attack&amp;quality=_100&amp;intercut=true&amp;caption=off"><b>here</b></a> 
                to see the documentary)</li>
                
                <li>Here Afro-American people seem not to agree with each other 
                (click <a href="http://media.cwi.nl/cocoon/cuypers/IWA/index.html?stage=two&amp;curlang=44&amp;stillLocation=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fdemo%2FVoxPopuli%2F&amp;RDFLocation=%2Fexport%2Fdata2%2Fmedia%2Fvideo%2FIWA%2Fns%2F&amp;repository=IWA&amp;domainNS=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FIWA%2FIWA.rdf%23&amp;local=true&amp;videolocation=rtsp%3A%2F%2Fmedia.cwi.nl%2Fmedia%2Fvideo%2FIWA%2Freal%2F&amp;Interviewees=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FIWA%2FIWA.rdf%23IWA_00093&amp;Opinions=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FVP%2FVoxPopuli.rdfs%23IWA_Instance_8&amp;Race2=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FVP%2FVoxPopuli.rdfs%23Black&amp;strategy=attack&amp;quality=_100&amp;intercut=true&amp;caption=off"><b>here</b></a> 
                to see the documentary)</li>

                <!--<li>The same lawyer in Harvard talks about the war in Afghanistan (still in favor of the military intervention). 
                Middle-aged and old white people too seem to have a different opinion 
                (click <a href="http://media.cwi.nl/cocoon/cuypers/IWA/index.html?stage=two&amp;curlang=44&amp;stillLocation=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fdemo%2FVoxPopuli%2F&amp;RDFLocation=%2Fexport%2Fdata2%2Fmedia%2Fvideo%2FIWA%2Fns%2F&amp;repository=IWA&amp;domainNS=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FIWA%2FIWA.rdf%23&amp;local=true&amp;videolocation=rtsp%3A%2F%2Fmedia.cwi.nl%2Fmedia%2Fvideo%2FIWA%2Freal%2F&amp;Interviewees=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FIWA%2FIWA.rdf%23IWA_00218&amp;Opinions=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FVP%2FVoxPopuli.rdfs%23IWA_Instance_8&amp;Age2=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FVP%2FVoxPopuli.rdfs%23Middleage&amp;Age2=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FVP%2FVoxPopuli.rdfs%23Old&amp;Race2=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FVP%2FVoxPopuli.rdfs%23White&amp;strategy=attack&amp;quality=_100&amp;intercut=true&amp;caption=off"><b>here</b></a> 
                to see the documentary)</li>-->

                <li>The same lawyer in Harvard talks about the war in Afghanistan (still in favor of the military intervention). 
								Young white people seem to disagree with her 
								(click <a href="http://media.cwi.nl/cocoon/cuypers/IWA/index.html?stage=two&amp;curlang=44&amp;stillLocation=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fdemo%2FVoxPopuli%2F&amp;RDFLocation=%2Fexport%2Fdata2%2Fmedia%2Fvideo%2FIWA%2Fns%2F&amp;repository=IWA&amp;domainNS=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FIWA%2FIWA.rdf%23&amp;local=true&amp;videolocation=rtsp%3A%2F%2Fmedia.cwi.nl%2Fmedia%2Fvideo%2FIWA%2Freal%2F&amp;Interviewees=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FIWA%2FIWA.rdf%23IWA_00218&amp;Opinions=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FVP%2FVoxPopuli.rdfs%23IWA_Instance_8&amp;Age2=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FVP%2FVoxPopuli.rdfs%23Teenager&amp;Age2=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FVP%2FVoxPopuli.rdfs%23Young&amp;Race2=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FVP%2FVoxPopuli.rdfs%23White&amp;strategy=attack&amp;quality=_100&amp;intercut=true&amp;caption=off"><b>here</b></a> 
                to see the documentary)</li>
                
                
                <li>The same lawyer in Harvard talks about the war in Afghanistan (still in favor of the military intervention). 
                This time young white people seem to agree with her 
                (click <a href="http://media.cwi.nl/cocoon/cuypers/IWA/index.html?stage=two&amp;curlang=44&amp;stillLocation=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fdemo%2FVoxPopuli%2F&amp;RDFLocation=%2Fexport%2Fdata2%2Fmedia%2Fvideo%2FIWA%2Fns%2F&amp;repository=IWA&amp;domainNS=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FIWA%2FIWA.rdf%23&amp;local=true&amp;videolocation=rtsp%3A%2F%2Fmedia.cwi.nl%2Fmedia%2Fvideo%2FIWA%2Freal%2F&amp;Interviewees=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FIWA%2FIWA.rdf%23IWA_00218&amp;Opinions=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FVP%2FVoxPopuli.rdfs%23IWA_Instance_8&amp;Age2=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FVP%2FVoxPopuli.rdfs%23Teenager&amp;Age2=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FVP%2FVoxPopuli.rdfs%23Young&amp;Race2=http%3A%2F%2Fwww.cwi.nl%2F%7Emedia%2Fns%2FVP%2FVoxPopuli.rdfs%23White&amp;strategy=support&amp;quality=_100&amp;intercut=true&amp;caption=off"><b>here</b></a> 
                to see the documentary)</li>
                

</ul>

                You can also try your own combinations. Please note that some combinations will get an empty result 
                (RealPlayer gives an error message: "SMIL: no &lt;smil&gt; tag found"). If you want to play with different 
                values you probably want to read <a href="#Explanation">this</a> first.
                <br/>
                <br/>



</div>

<div id="Body">
              
              <form
                action="index.html"
                method="get"
                enctype="application/x-www-form-urlencoded">

                <div>
                  <input type="hidden" name="stage">
                    <xsl:attribute name="value">two</xsl:attribute>
                  </input>
                  <xsl:call-template name="hiddeninput"/>
                </div>

                <table>
                  <tr>
                    <th>Question</th>
                    <th>Interviewee</th>
                    <th>Position</th>
                  </tr>
                  <tr>
                    <td>
                      <select size="6" name="Questions" >
                        <xsl:attribute name="onClick">
                          this.form.stage.value='one';
                          this.form.Opinions.selectedIndex=-1;
                          this.form.submit();
                        </xsl:attribute>
                        <xsl:attribute name="multiple">multiple</xsl:attribute>
                        <!--<select size="8" onChange="this.form.submit();" name="Concepts" >-->
                          <xsl:apply-templates select="QuestionList/Question">
                            <xsl:with-param name="Number" >1</xsl:with-param>
                          </xsl:apply-templates>
                        </select>
                      </td>
                      <td>
                        <select size="6" name="Interviewees" >
                        <xsl:attribute name="onClick">
                          this.form.stage.value='one';
                          this.form.submit();
                        </xsl:attribute>
                          <xsl:attribute name="multiple">multiple</xsl:attribute>
                          <!--<select size="8" onChange="this.form.submit();" name="Opinions" >-->
                            <xsl:apply-templates select="IntervieweeList/Interviewee">
                              <xsl:with-param name="Number" >1</xsl:with-param>
                            </xsl:apply-templates>
                          </select>
                        </td>
                      <td>
                        <select size="6" name="Opinions" >
                          <xsl:attribute name="onClick">this.form.stage.value='one';
                            this.form.Questions.selectedIndex=-1;
                            this.form.submit();
                          </xsl:attribute>
                          <xsl:attribute name="multiple">multiple</xsl:attribute>
                          <!--<select size="8" onChange="this.form.submit();" name="Opinions" >-->
                            <xsl:apply-templates select="OpinionList/Opinion">
                              <xsl:with-param name="Number" >1</xsl:with-param>
                            </xsl:apply-templates>
                          </select>
                        </td>
                      </tr>
                    </table>

                    <table>
                      <tr>
                        <xsl:apply-templates select="SocClassList/SocClass" mode="header">
                          <xsl:with-param name="Number" >1</xsl:with-param>
                        </xsl:apply-templates>
                      </tr>
                      <tr>
                        <xsl:apply-templates select="SocClassList/SocClass" mode="body">
                          <xsl:with-param name="Number" >1</xsl:with-param>
                        </xsl:apply-templates>
                      </tr>
                    </table>

                    <table>
                      <tr>
                        <xsl:apply-templates select="SocClassList/SocClass" mode="header">
                          <xsl:with-param name="Number" >2</xsl:with-param>
                        </xsl:apply-templates>
                      </tr>
                      <tr>
                        <xsl:apply-templates select="SocClassList/SocClass" mode="body">
                          <xsl:with-param name="Number" >2</xsl:with-param>
                        </xsl:apply-templates>
                      </tr>
                    </table>

                    <hr style="clear: both;"/>
                    <table>
                      <tr>
                        <th>Point Of View</th>
                        <th>Bandwidth</th>
                        <th>Intercut</th>
                        <th>Captions</th>
                      </tr>
                      <tr>
                        <td>
                          <input type="radio" name="strategy" value="none">
                            <!--<xsl:attribute name="onClick">this.form.submit();</xsl:attribute> -->
                            <xsl:if test="$Strategy = 'none'">
                              <xsl:attribute name="checked">checked</xsl:attribute>
                            </xsl:if>
                            Do not force a point of view
                          </input>
                          <br />
                          <input type="radio" name="strategy" value="attack">
                            <!--<xsl:attribute name="onClick">this.form.submit();</xsl:attribute> -->
                            <xsl:if test="$Strategy = 'attack'">
                              <xsl:attribute name="checked">checked</xsl:attribute>
                            </xsl:if>
                            Propagandist - Create Clash
                          </input>
                          <br />
                          <input type="radio" name="strategy" value="support">
                            <!--<xsl:attribute name="onClick">this.form.submit();</xsl:attribute> -->
                            <xsl:if test="$Strategy = 'support'">
                              <xsl:attribute name="checked">checked</xsl:attribute>
                            </xsl:if>
                            Propagandist - Create Support
                          </input>
                          <br />
                          <input type="radio" name="strategy" value="VP">
                            <!--<xsl:attribute name="onClick">this.form.submit();</xsl:attribute> -->
                            <xsl:if test="$Strategy = 'VP'">
                              <xsl:attribute name="checked">checked</xsl:attribute>
                            </xsl:if>
                            Binary Communicator
                          </input>
                        </td>
                        <td>
                          <input type="radio" name="quality" value="_20">
                            <!--<xsl:attribute name="onClick">this.form.submit();</xsl:attribute> -->
                            <xsl:if test="$Quality = '_20'">
                              <xsl:attribute name="checked">checked</xsl:attribute>
                            </xsl:if>
                            Low Bandwidth
                          </input>
                          <br />
                          <input type="radio" name="quality" value="_100">
                            <!--<xsl:attribute name="onClick">this.form.submit();</xsl:attribute> -->
                            <xsl:if test="$Quality = '_100'">
                              <xsl:attribute name="checked">checked</xsl:attribute>
                            </xsl:if>
                            Medium Bandwidth
                          </input>
                          <br />
                          <input type="radio" name="quality" value="_225">
                            <!--<xsl:attribute name="onClick">this.form.submit();</xsl:attribute> -->
                            <xsl:if test="$Quality = '_225'">
                              <xsl:attribute name="checked">checked</xsl:attribute>
                            </xsl:if>
                            High Bandwidth
                          </input>
                        </td>
                        <td>
                          <input type="radio" name="intercut" value="true">
                            <!--<xsl:attribute name="onClick">this.form.submit();</xsl:attribute> -->
                            <xsl:if test="$Intercut = 'true'">
                              <xsl:attribute name="checked">checked</xsl:attribute>
                            </xsl:if>
                            True
                          </input>
                          <br />
                          <input type="radio" name="intercut" value="false">
                            <!--<xsl:attribute name="onClick">this.form.submit();</xsl:attribute> -->
                            <xsl:if test="$Intercut = 'false'">
                              <xsl:attribute name="checked">checked</xsl:attribute>
                            </xsl:if>
                            False
                          </input>
                          <br />
                        </td>
                        <td>
                          <input type="radio" name="caption" value="on">
                            <!--<xsl:attribute name="onClick">this.form.submit();</xsl:attribute> -->
                            <xsl:if test="$Caption = 'on'">
                              <xsl:attribute name="checked">checked</xsl:attribute>
                            </xsl:if>
                            On (can cause problems)
                          </input>
                          <br />
                          <input type="radio" name="caption" value="off">
                            <!--<xsl:attribute name="onClick">this.form.submit();</xsl:attribute> -->
                            <xsl:if test="$Caption = 'off'">
                              <xsl:attribute name="checked">checked</xsl:attribute>
                            </xsl:if>
                            Off
                          </input>
                        </td>
                      </tr>
                    </table>

                    <input type="submit" value="Done">
                      <xsl:attribute name="class">
                        <xsl:value-of select="$LabelSubmit"/>
                      </xsl:attribute>
                    </input>

                    <input type="reset" value="Reset">
                    </input>

                  </form>
                  <hr style="clear: both;"/>

                  Please note that if the above button is <span class="colorlabel">this color</span> AND you have selected a Point Of View different from "None", then you will have to wait about 90 seconds
                  after pressing it.

                  <div style="float: left;">RDF storage and querying by
                    <a href="http://www.openrdf.org">Sesame</a>
                  </div>

                  <hr style="clear: both;"/>
                  <h2><a name="Explanation">Short Guide to the Demo</a></h2>

Vox Populi can generate non-rhetorical (categorical) and rhetorical documentaries.

<h3>Categorical documentaries</h3>

<p>Categorical documentaries are generated with the option <b>None</b> as Point of View. The selection of the content (<b>group A</b>) can be done as follows.

 At the top of the window, three select boxes contain the questions
 asked during the interviews, the interviewees and the opinions
 expressed in the material contained in the repository. The second and
 third rows are equal and present a list of select boxes corresponding
 to the social categories of the interviewees. The viewer can choose to see how one or
 more questions have been answered in the material, or what one or
 more interviewees have said, or all clips expressing one or more
 opinions. The viewer can also combine two selections toghether by
 specifying the interviewee(s) and either the question(s) asked or the
 opinion(s) expressed (since opinions are expressed as a result of
 questions being asked to interviewees, the viewer can not select
 both). Possible selections are:


<ul>

  <li>select one or more <b>questions</b>. All the clips where
  this/these question(s) is/are answered are retrieved. If the viewer
  selects also one or more interviewees, only clips showing that/those
  interviewee(s) are selected.
 </li>

  <li>select one or more <b>positions</b>. All the clips where
  this/these position(s) is/are expressed are retrieved. If the viewer
  selects also one or more interviewees, only clips showing that/those
  interviewee(s) are selected.
  </li>

  <li>select one or more <b>interviewees</b> by name. All the
  clips where this/these interviewee(s) is/are shown are retrieved. If
  the viewer selects also one or more questions, only clips showing
  that/those questions(s) are selected. Otherwise, if the viewer
  selects also one or more positions, only clips showing that/those
  positions(s) are selected.
  </li>

  <li>select <b>a class of interviewees</b> by categories, by
  selecting in the second row the social categories the interviewees
  belog to. All the clips where this class of interviewees is shown
  are retrieved. If the viewer selects also one or more questions,
  only clips showing that/those questions(s) are selected. Otherwise,
  if the viewer selects also one or more positions, only clips showing
  that/those positions(s) are selected.
  </li>
</ul>

 </p>
 
<h3>Rhetorical documentaries</h3>

 <p>For a rhetorical documentary, the viewer needs to select the other group (<b>group B</b>) that will support or counterargue the statements expressed by <b>group A</b>, which can be selected as specified above. <b>group A</b> can also be a single interviewee, <b>group B</b> is always a class of interviewees,
 which can be selected from the classes in the third row of select
 boxes. A rhetorical documentary can have three different
 point of view:
 <ul>
 
 <li><b>Propagandist - Create clash</b> causes
 Vox Populi to present <b>group A</b> being counterargued by <b>group B</b>.
 </li>

 <li><b>Propagandist - Create support</b>, Vox Populi present <b>group A</b> being supported by
 <b>group B</b>.
 </li>
 
  <li><b>Binary Communicator</b>. The position of a main character is complemented with statements of other
 interviewees. The viewer selects the main
 characters in the first row (<b>group A</b>), and <b>group B</b> and <b>group C</b> are selected in
 the second and third rows, respectively. The generated documentary
 presents the main character, followed by <b>group B</b> supporting it
 and then <b>group C</b> producing the counterargument.
 </li>
 
 </ul>

</p>

 <h3>General Options</h3>

<p>

<ul>

 <li><b>bandwidth</b> of the connection, Low, Medium and
 High. This option causes Vox Populi to use video clips rendered at
 20kbs, 100kbs and 225kbs, respectively.
 </li>

 <li><b>Intercut</b>, on or off. If Intercut is off, Vox Populi
 presents <b>group A</b>'s video clips entirely, and then <b>group
 B</b>'s and <b>group C</b>'s. If Intercut is on, <b>group A</b>'s clips are cut in between by
 <b>group B</b>'s clips. This second presentation mode
 generates more dynamic documentaries since the length of the clips is
 shorter.
 </li>

 <li><b>Captions</b>, on or off. If the captions are on,
 additional information is displayed in the caption area, such as the question asked, the interviewee
 or her statements. This options is necessary due to the fact that
 captions create sometimes problems in Real Player.
 </li>

</ul>

</p>
</div>
                </body>
              </html>
            </xsl:if>



            <xsl:if test="$Stage = 'two'">

              <!-- copy smil content -->
              <xsl:copy-of select="/page/vpoutput/*" />
            </xsl:if>

          </xsl:template>

        </xsl:stylesheet>
