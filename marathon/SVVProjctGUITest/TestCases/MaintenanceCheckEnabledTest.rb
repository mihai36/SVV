#{{{ Marathon
require_fixture 'default'
#}}} Marathon

severity("normal")

def test

    with_window("") {
        assert_p("Switch to maintenance mode", "Enabled", "false")
        click("Start")
        assert_p("Switch to maintenance mode", "Enabled", "true")
        click("Stop")
        assert_p("Switch to maintenance mode", "Enabled", "false")
    }

end
