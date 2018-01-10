package com.jameskbride.eventbusmvpdemo.security

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.jameskbride.eventbusmvpdemo.R
import com.jameskbride.eventbusmvpdemo.security.SecurityErrorViewPresenter.SecurityErrorView
import javax.inject.Inject

class SecurityErrorViewFragmentImpl @Inject
constructor(val presenter: SecurityErrorViewPresenter) : SecurityErrorView {
    private var networkErrorViewFragment: SecurityErrorViewFragment? = null

    fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?, securityErrorViewFragment: SecurityErrorViewFragment): View {
        this.networkErrorViewFragment = securityErrorViewFragment
        val view = inflater?.inflate(R.layout.security_error, container)
        presenter.view = this

        view?.findViewById<Button>(R.id.ok_button)?.setOnClickListener {view: View? ->
            presenter.dismiss()
        }

        return view!!
    }

    fun onResume(securityErrorViewFragment: SecurityErrorViewFragment) {}

    fun onPause(securityErrorViewFragment: SecurityErrorViewFragment) {}

    override fun dismiss() {
        networkErrorViewFragment!!.dismiss()
    }
}