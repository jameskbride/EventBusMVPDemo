package com.jameskbride.eventbusmvpdemo

import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.jameskbride.eventbusmvpdemo.main.MainActivity
import com.jameskbride.eventbusmvpdemo.pages.NetworkErrorPage
import com.jameskbride.eventbusmvpdemo.pages.ProfilePage
import com.jameskbride.eventbusmvpdemo.pages.SecurityErrorPage
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import shouldDisplay
import shouldDisplayTextFor
import waitOn

@RunWith(AndroidJUnit4::class)
class ProfilePageTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java,true, true)

    private lateinit var profilePage: ProfilePage
    private lateinit var networkErrorPage: NetworkErrorPage
    private lateinit var securityErrorPage: SecurityErrorPage

    @Before
    fun setUp() {
        this.profilePage = ProfilePage()
        this.networkErrorPage = NetworkErrorPage()
        this.securityErrorPage = SecurityErrorPage()
    }

    @Test
    fun whenTheViewLoadsItDisplaysTheSearchView() {
        shouldDisplay(profilePage.profileEditButton())
    }

    @Test
    fun givenAGoodProfileIdItDisplaysTheProfile() {
        val profileId = "1"
        profilePage.enterProfileId(profileId)

        profilePage.submitProfileId()

        shouldDisplayTextFor(profilePage.customerName(), "Walter White")
        shouldDisplayTextFor(profilePage.addressLine1(),"123 Street Ln")
        shouldDisplayTextFor(profilePage.city(),"Albuquerque")
        shouldDisplayTextFor(profilePage.state(),"NM")
        shouldDisplayTextFor(profilePage.zip(),"87101")
    }

    @Test
    fun givenAGoodProfileIdItDisplaysTheOrders() {
        profilePage.enterProfileId("1")

        profilePage.submitProfileId()

        profilePage.displaysOrderText("Large Pizza")
        profilePage.displaysOrderText("Industrial size beaker")
        profilePage.displaysOrderText("Barrel of Methylamine")
    }

    @Test
    fun givenAnInvalidProfileIdItDisplaysTheNetworkErrorView() {
        profilePage.enterProfileId("-1")

        profilePage.submitProfileId()

        waitOn {
            networkErrorPage.displaysErrorMessage("Oops, something went wrong!")
            shouldDisplay(networkErrorPage.retryButton())
        }
    }

    @Test
    fun givenTheSecurityCheckFailedItDisplaysTheSecurityErrorView() {
        profilePage.enterProfileId("2")

        profilePage.submitProfileId()

        waitOn {
            securityErrorPage.displaysSecurityMessage("Please log in")
            shouldDisplay(securityErrorPage.okButton())
        }
    }
}
