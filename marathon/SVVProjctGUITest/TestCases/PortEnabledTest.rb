#{{{ Marathon
require_fixture 'default'
#}}} Marathon

severity("normal")

def test

    with_window("") {
        click("Start")
        assert_p("Server listening port", "Enabled", "false")
        click("Stop")
    }

end
