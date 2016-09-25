<?xml version="1.0"?>
<!DOCTYPE xsl:stylesheet [
<!ENTITY delcon   SYSTEM "../transform/delcon.xml">
<!ENTITY smilpref SYSTEM "../transform/smilpref.xml">
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
  <xsl:variable name="Stage"><xsl:value-of select="/page/selected_values/stage"/></xsl:variable>
  <xsl:variable name="Quality"><xsl:value-of select="/page/selected_values/quality"/></xsl:variable>
  <xsl:variable name="Caption"><xsl:value-of select="/page/selected_values/caption"/></xsl:variable>

  <!-- VJ specific variables  -->

  <xsl:variable name="SelQuest"><xsl:value-of select="/page/selected_values/selectedquestion"/></xsl:variable>
  <xsl:variable name="SelVJ"><xsl:value-of select="/page/selected_values/selectedVJ"/></xsl:variable>
  <xsl:variable name="ArrIndex"><xsl:value-of select="/page/selected_values/arrayindex"/></xsl:variable>
  <xsl:variable name="Index0"><xsl:value-of select="/page/selected_values/index0"/></xsl:variable>
  <xsl:variable name="Index1"><xsl:value-of select="/page/selected_values/index1"/></xsl:variable>
  <xsl:variable name="Index2"><xsl:value-of select="/page/selected_values/index2"/></xsl:variable>


  <!-- TEMPLATES -->

  <xsl:template match="title"/>
  <xsl:template match="thanks"/>

  <xsl:template match="qlabel"/>
  <xsl:template match="plabel"/>

  <!-- generate option element named after Id and label -->
  <xsl:template match="question" mode="first" >
    <xsl:variable name="Id"><xsl:value-of select="./qid"/></xsl:variable>
    <option>
      <xsl:variable name="selected"><xsl:value-of select="./selected"/></xsl:variable>
      <xsl:if test="$selected">
        <xsl:attribute name="selected">selected</xsl:attribute>
      </xsl:if>
      <xsl:attribute name="value">
        <xsl:value-of select="normalize-space($Id)"/>
      </xsl:attribute>
      <xsl:value-of select="./qlabel"/>
      <xsl:apply-templates select="question" />
    </option>
  </xsl:template>

  <!-- generate option element named after Id and label -->
  <xsl:template match="partecipant" mode="first" >
    <xsl:variable name="Id"><xsl:value-of select="./pid"/></xsl:variable>
    <option>
      <xsl:variable name="selected"><xsl:value-of select="./selected"/></xsl:variable>
      <xsl:if test="$selected">
        <xsl:attribute name="selected">selected</xsl:attribute>
      </xsl:if>
      <xsl:attribute name="value">
        <xsl:value-of select="normalize-space($Id)"/>
      </xsl:attribute>
      <xsl:value-of select="./plabel"/>
      <xsl:apply-templates select="partecipant" />
    </option>
  </xsl:template>

  <!-- generate option element named after Id and label -->
  <xsl:template match="question" mode="second" >
    <xsl:variable name="selected"><xsl:value-of select="./selected"/></xsl:variable>
    <xsl:if test="$selected">
      <input type="hidden" name="question">
        <xsl:variable name="Id"><xsl:value-of select="./qid"/>
        </xsl:variable>
        <xsl:attribute name="value">
          <xsl:value-of select="normalize-space($Id)"/>
        </xsl:attribute>
      </input>
    </xsl:if>
    <xsl:apply-templates select="question" mode="second" />
  </xsl:template>

  <!-- generate option element named after Id and label -->
  <xsl:template match="partecipant" mode="second" >
    <xsl:variable name="selected"><xsl:value-of select="./selected"/></xsl:variable>
    <xsl:if test="$selected">
      <input type="hidden" name="VJ">
        <xsl:variable name="Id"><xsl:value-of select="./pid"/>
        </xsl:variable>
        <xsl:attribute name="value">
          <xsl:value-of select="normalize-space($Id)"/>
        </xsl:attribute>
      </input>
    </xsl:if>
    <xsl:apply-templates select="partecipant" mode="second" />
  </xsl:template>


  <!-- generate select for questions -->
  <xsl:template match="questionsList"  mode="first">
    <select class="breedtegoed">
      <xsl:attribute name="multiple">multiple</xsl:attribute>
      <xsl:attribute name="size">13</xsl:attribute>
      <xsl:attribute name="onChange">this.form.submit();</xsl:attribute>
      <xsl:attribute name="name">question</xsl:attribute>
      <xsl:apply-templates mode="first" />
    </select>
  </xsl:template>

  <!-- generate select for partecipants -->
  <xsl:template match="partecipantList"  mode="first">
    <select class="breedteklein">
      <xsl:attribute name="multiple">multiple</xsl:attribute>
      <xsl:attribute name="size">13</xsl:attribute>
      <xsl:attribute name="onChange">this.form.submit();</xsl:attribute>
      <xsl:attribute name="name">VJ</xsl:attribute>
      <xsl:apply-templates mode="first" />
    </select>
  </xsl:template>

  <xsl:template name="commonhiddeninput">

    <input type="hidden" name="curlang">
      <xsl:attribute name="value">
        <xsl:value-of select="$curlang"/>
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

  <!-- generate caption input for form -->
  <xsl:template name="captioninput">
    <input type="radio" name="caption" value="on">
      <xsl:attribute name="onClick">this.form.submit();</xsl:attribute>
      <xsl:if test="$Caption = 'on'">
        <xsl:attribute name="checked">checked</xsl:attribute>
      </xsl:if>
      Aan
    </input>
    <input type="radio" name="caption" value="off">
      <xsl:attribute name="onClick">this.form.submit();</xsl:attribute>
      <xsl:if test="$Caption = 'off'">
        <xsl:attribute name="checked">checked</xsl:attribute>
      </xsl:if>
      Uit
    </input>

  </xsl:template>

  <!-- generate quality input for form -->
  <xsl:template name="qualityinput">
    <input type="radio" name="quality" value="_20">
      <xsl:attribute name="onClick">this.form.submit();</xsl:attribute>
      <xsl:if test="$Quality = '_20'">
        <xsl:attribute name="checked">checked</xsl:attribute>
      </xsl:if>
      Laag

    </input>
    <input type="radio" name="quality" value="_100">
      <xsl:attribute name="onClick">this.form.submit();</xsl:attribute>
      <xsl:if test="$Quality = '_100'">
        <xsl:attribute name="checked">checked</xsl:attribute>
      </xsl:if>
      Medium
    </input>
    <input type="radio" name="quality" value="_225">
      <xsl:attribute name="onClick">this.form.submit();</xsl:attribute>
      <xsl:if test="$Quality = '_225'">
        <xsl:attribute name="checked">checked</xsl:attribute>
      </xsl:if>
      Hoog
    </input>
  </xsl:template>

  <!-- generate body of the HTML page -->
  <xsl:template match="page">
    <xsl:if test="$Stage = 'one'">
      <html>
        <head>
          <link
            type="text/css"
            rel="stylesheet"
            href="../VP/stijl.css" />
            <title>VOXPOPULI</title>
          </head>

          <body>
            <div id="vid" style="position:absolute; left:9px; height:139px; z-index:3;" >
              <table class="out">
                <form
                  action="index.html"
                  method="get"
                  enctype="application/x-www-form-urlencoded"
                  name="firstform">


                  <input type="hidden" name="stage">
                    <xsl:attribute name="value">one</xsl:attribute>
                  </input>
                  <input type="hidden" name="arrayindex">
                  </input>
                  <input type="hidden" name="index0">
                  </input>
                  <input type="hidden" name="index1">
                  </input>
                  <input type="hidden" name="index2">
                  </input>
                  <xsl:call-template name="commonhiddeninput"/>

                  <tr>
                    <td valign='top'><xsl:apply-templates select="/page/questionsList" mode="first"/></td>
                    <td valign='top' colspan="2"><xsl:apply-templates select="/page/partecipantList" mode="first"/></td>
                  </tr>
                  <tr>
                    <td valign="top" align="left">Bandbreedte:
                      <xsl:call-template name="qualityinput"/>
                    </td>
                    <td>
                    <!--Ondertitels:
                      <xsl:call-template name="captioninput"/>
                    -->
                    </td>
                  </tr>
                  <tr>
                    <td><input type="reset" class="reset" value="Reset"/></td>
                  </tr>
                </form>
                <td>
                  <xsl:if test="(string($SelQuest) and string($SelVJ))">
                    <form
                      action="index.html"
                      method="get"
                      enctype="application/x-www-form-urlencoded"
                      name="secondform">

                      <div>
                        <input type="hidden" name="quality">
                          <xsl:attribute name="value">
                            <xsl:value-of select="$Quality"/>
                          </xsl:attribute>
                        </input>

                        <input type="hidden" name="stage">
                          <xsl:attribute name="value">two</xsl:attribute>
                        </input>
                        <input type="hidden" name="caption">
                          <xsl:attribute name="value">
                            <xsl:value-of select="$Caption"/>
                          </xsl:attribute>
                        </input>

                        <xsl:call-template name="commonhiddeninput"/>
                        <xsl:apply-templates select="/page/questionsList/question" mode="second" />
                        <xsl:apply-templates select="/page/partecipantList/partecipant" mode="second" />
                        <input type="submit" class="reset" value="Play" />
                      </div>
                    </form>

                  </xsl:if>
                </td>

              </table>
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
