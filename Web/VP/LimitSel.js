<script type="text/javascript">
&lt;!--


var debugCounter = 0;
function debugMsg(msg) {
    // un rem the next line when you go live to prevent debug messages from showing
    // return;
    debugCounter++;
    var t = document.createTextNode("\n" + debugCounter + ": " + msg);
    var br = document.createElement("br");
    var debug = document.getElementById("debug");
    debug.appendChild(t);
    debug.appendChild(br);
}

var index = <xsl:value-of select="normalize-space($ArrIndex)"/>;
var a = new Array("<xsl:value-of select="normalize-space($Index0)"/>",
                  "<xsl:value-of select="normalize-space($Index1)"/>",
                  "<xsl:value-of select="normalize-space($Index2)"/>");

function LimitSel() {

    var found;


    for( var Current=0;Current &lt; document.firstform.question.options.length;Current++ ) {
      if( document.firstform.question.options[Current].selected == true ){
        found = 0;
        for( var ArIn=0; ArIn &lt; 3; ArIn++ ) {
          if( Current == a[ArIn] ) {
            found = 1;
            break;
          }
        }
        if( found == 0 ){
          a[index] = Current;
          index++;
          if( index &gt; 2 ){
            index = 0;
          }
        }
      }
    }
    for( var Current=0;Current &lt; document.firstform.question.options.length;Current++ ) {

      for( var ArIn=0; ArIn &lt; 3; ArIn++ ) {
        if( Current == a[ArIn] ) {
          document.firstform.question.options[Current].selected = true;
          break;
        }else{
          document.firstform.question.options[Current].selected = false;
        }
      }

    }

<!--
  for( var ArIn=0; ArIn &lt; 3; ArIn++ ) {
    debugMsg(a[ArIn] + " is selected");
  }
  debugMsg("Index is " + index);
    document.firstform.appendChild(value);
        var inputelem = "&lt;input name='arrayindex' type='hidden' value='" + index + "'&gt;";
    debugMsg(inputelem);
    var input = document.createElement(inputelem);
    var value = document.createTextNode(index + " cazzo");

    document.firstform.appendChild(input);

    var inputelem = "&lt;input name='arrayindex' type='hidden' value='" + index + "'&gt;";
    debugMsg(inputelem);

    var input = document.createElement(inputelem);
        var inputelem = "&lt;value='" + index + "'&gt;";

            debugMsg(inputelem);
                var inarrind = document.getElementById("arrayindex");

    inarrind.setAttribute("value", "" + index);
    debugMsg("Index is " + index);
-->

    document.firstform.arrayindex.value = index;
    document.firstform.index0.value = a[0];
    document.firstform.index1.value = a[1];
    document.firstform.index2.value = a[2];

    document.firstform.submit();

}

//--&gt;

</script>


<!--
        <span id="debug"></span>
        <form><input type="button" value="Try It" onclick="debugMsg('Bugs make me spit');" /></form>
-->


