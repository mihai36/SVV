#{{{ Marathon
require_fixture 'default'
#}}} Marathon

severity("normal")

def test

    with_window("") {
        select("Web root directory", "./htdocs2/")
        click("Start")
        assert_p("Web root directory", "Enabled", "false")
        click("Stop")
    }

end
