#{{{ Marathon
require_fixture 'default'
#}}} Marathon

severity("normal")

def test
      with_window("") {
        doubleclick("Server listening port")
        select("Server listening port", "10077")
        click("Start")
        assert_p("lbl:10077", "Text", "10077")
        click("Stop")
    }

end
