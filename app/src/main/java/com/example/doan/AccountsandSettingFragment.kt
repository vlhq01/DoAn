package com.example.doan


import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.doan.FireBaseUserLiveData.ViewModel.ProjectViewModel
import com.example.doan.databinding.AccountsAndSettingFragmentBinding
import com.firebase.ui.auth.AuthUI


class AccountsandSettingFragment : Fragment() {
    private val viewModel by viewModels<ProjectViewModel>()
    private var _binding: AccountsAndSettingFragmentBinding?=null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    companion object {
        const val TAG = "SettingFragment"
        const val SIGN_IN_REQUEST_CODE = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        observeAuthenticationState()
        val mainViewModel = (activity as MainActivity).mainViewModel
        mainViewModel.phone.observe(viewLifecycleOwner, Observer {
            binding.txtWalletPhone.text = it.toString()

        })
        _binding = AccountsAndSettingFragmentBinding.inflate(inflater,container,false)
        val view = binding.root
        binding.btnsignin.setOnClickListener(View.OnClickListener {
            val action = AccountsandSettingFragmentDirections.actionAccountsandSettingFragmentToSignInFragment()
            view.findNavController().navigate(action)
        })
        binding.settingConstraintLayout.setOnClickListener {
            val action = AccountsandSettingFragmentDirections.actionAccountsandSettingFragmentToSettingServicesFragment()
            findNavController().navigate(action)
        }
        return view
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
                binding.btnsignin.setOnClickListener(View.OnClickListener {
                    val action = AccountsandSettingFragmentDirections.actionAccountsandSettingFragmentToSignInFragment()
                    view?.findNavController()?.navigate(action)
                })

            }
        }
        })
        //
        //
    }
}