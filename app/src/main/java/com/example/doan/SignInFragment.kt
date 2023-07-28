package com.example.doan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.doan.FireBaseUserLiveData.ViewModel.ProjectViewModel
import com.example.doan.databinding.AccountsAndSettingFragmentBinding
import com.example.doan.databinding.SignInBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class SignInFragment : Fragment() {
    private val viewModel by viewModels<ProjectViewModel>()
    private var _binding: SignInBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        observeAuthenticationState()
        _binding = SignInBinding.inflate(inflater,container,false)

        var navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomnavigation)
        if (navBar != null) {
            navBar.isVisible = false
        }
        val view = binding.root
        binding.btnsignin.setOnClickListener(View.OnClickListener { launchSignInFlow() })
        return view
    }

    private fun launchSignInFlow() {
        // TODO Complete this function by allowing users to register and sign in with
        //  either their email address or Google account.
        // Give users the option to sign in / register with their email or Google account.
        // If users choose to register with their email,
        // they will need to create a password as well.
        val providers = arrayListOf(
            AuthUI.IdpConfig.PhoneBuilder().setDefaultNumber("+1" + binding.edtPersonNumber.getText().toString()).build()

            // This is where you can provide more ways for users to register and
            // sign in.
        )

        // Create and launch the sign-in intent.
        // We listen to the response of this activity with the
        // SIGN_IN_REQUEST_CODE.

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            AccountsandSettingFragment.SIGN_IN_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // TODO Listen to the result of the sign in process by filter for when
        //  SIGN_IN_REQUEST_CODE is passed back. Start by having log statements to know
        //  whether the user has signed in successfully
        if (requestCode == AccountsandSettingFragment.SIGN_IN_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // User successfully signed in.
                Log.i(AccountsandSettingFragment.TAG, "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!")
            } else {
                // Sign in failed. If response is null, the user canceled the
                // sign-in flow using the back button. Otherwise, check
                // the error code and handle the error.
                Log.i(AccountsandSettingFragment.TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }

    private fun observeAuthenticationState() {


        // TODO 1. Use the authenticationState variable you just added
        // in LoginViewModel and change the UI accordingly.
        viewModel.authenticationState.observe(viewLifecycleOwner, Observer {
                authenticationState -> when(authenticationState){
            // TODO 2. If the user is logged in,
            // you can customize the welcome message they see by
            // utilizing the getFactWithPersonalization() function provided
            ProjectViewModel.AuthenticationState.AUTHENTICATED -> {
                binding.btnsignin.text= getString(R.string.logout_button_text)
                findNavController().popBackStack()
                binding.btnsignin.setOnClickListener(){
                    // TODO implement logging out user in next step
                    AuthUI.getInstance().signOut(requireContext())
                }


            }
            else ->{
                // TODO 3. Lastly, if there is no logged-in user,
                // auth_button should display Login and
                //  launch the sign in screen when clicked.
                binding.btnsignin.text = getString(R.string.login_button_text)
                binding.btnsignin.setOnClickListener { launchSignInFlow() }

            }
        }
        })
        //
        //  TODO If there is a logged-in user, authButton should display Logout. If the
        //   user is logged in, you can customize the welcome message by utilizing
        //   getFactWithPersonalition(). I

        // TODO If there is no logged in user, authButton should display Login and launch the sign
        //  in screen when clicked. There should also be no personalization of the message
        //  displayed.
    }
}