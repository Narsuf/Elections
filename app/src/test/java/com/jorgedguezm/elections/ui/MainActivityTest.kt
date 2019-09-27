package com.jorgedguezm.elections.ui

/*@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    private var elections: List<Election>? = null
    private var parties: List<Party>? = null

    @Before
    fun fillFields() {
        ActivityScenario.launch(SplashActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                elections = activity.electionsViewModel.electionsResult().value
                sleep(1000)
            }

            scenario.onActivity { parties = it.electionsViewModel.partiesResult().value }
        }
    }

    @Test
    @LooperMode(LooperMode.Mode.PAUSED)
    fun launchMainActivity() {
        ActivityScenario.launch(SplashActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val myIntent = Intent(activity, MainActivity::class.java)
                val electionsParams = Bundle()

                electionsParams.putSerializable(KEY_ELECTIONS, ArrayList(elections))
                electionsParams.putSerializable(KEY_PARTIES, ArrayList(parties))

                myIntent.putExtras(electionsParams)

                ActivityScenario.launch<Activity>(myIntent).use { scenario ->
                    scenario.onActivity { activity ->
                        val fragmentPagerAdapter = activity.container.adapter as FragmentPagerAdapter
                        assertEquals(fragmentPagerAdapter.count, 3)
                        sleep(1000)
                    }
                }
            }
        }
    }

    @Test
    @LooperMode(LooperMode.Mode.PAUSED)
    fun launchMainFragmentWithJustOneElectionInArguments() {
        ActivityScenario.launch(SplashActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val myIntent = Intent(activity, MainActivity::class.java)
                val electionsParams = Bundle()

                electionsParams.putSerializable(KEY_PARTIES, ArrayList(parties))
                electionsParams.putSerializable(KEY_ELECTIONS, arrayListOf(
                        generateStoredCongressElection(), generateStoredSenateElection()))

                myIntent.putExtras(electionsParams)

                ActivityScenario.launch<Activity>(myIntent).use { it.onActivity { sleep(1000) } }
            }
        }
    }
}*/