#{{{ Marathon
require_fixture 'default'
#}}} Marathon

severity("normal")

def test

    with_window("") {
        select("Maintenance directory", "./htdocs2/")
        click("Start")
        assert_p("Maintenance directory", "Enabled", "false")
        click("Stop")
    }

end
