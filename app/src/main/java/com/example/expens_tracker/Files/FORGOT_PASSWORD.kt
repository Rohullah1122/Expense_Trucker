package com.example.expens_tracker.Files

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expens_tracker.R
import com.example.expens_tracker.ViewModel.ForgotPasswordState
import com.example.expens_tracker.ViewModel.ForgotPasswordViewModel
import com.example.expens_tracker.ui.theme.AppTheme

@Composable
fun ForgotPasswordUI(navController: NavController) {

    val viewModel: ForgotPasswordViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val state by viewModel.state.collectAsState()

    var email by remember { mutableStateOf("") }
    val dimens = AppTheme.dimens

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Decorative Top Right Glow
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopEnd)
                .offset(x = 50.dp, y = (-50).dp)
                .blur(80.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = dimens.extraLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon Header
            Box(
                modifier = Modifier
                    .size(dimens.logoSize)
                    .background(
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                        RoundedCornerShape(dimens.roundedCorner)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LockReset,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(dimens.large))

            Text(
                text = stringResource(R.string.forgot_password_title),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(dimens.small))

            Text(
                text = stringResource(R.string.forgot_password_subtitle),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = dimens.small)
            )

            Spacer(modifier = Modifier.height(dimens.extraLarge))

            // Email Input using the same glass style
            RegisterGlassInput(
                value = email,
                onValueChange = { email = it },
                label = stringResource(R.string.email_label),
                placeholder = stringResource(R.string.email_placeholder),
                icon = Icons.Default.Email
            )

            Spacer(modifier = Modifier.height(dimens.extraLarge))

            // Action Button
            Button(
                onClick = { viewModel.sendResetEmail(email) }
                ,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.buttonHeight)
                    .clip(RoundedCornerShape(dimens.roundedCorner))
                    .background(
                        Brush.horizontalGradient(
                            listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                        )
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(
                    text = stringResource(R.string.reset_button),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            LaunchedEffect(state) {
                when (state) {
                    is ForgotPasswordState.Success -> {
                        // Navigate back to login
                        navController.popBackStack()
                    }
                    is ForgotPasswordState.Error -> {
                        // TODO: show snackbar or toast
                    }
                    else -> {}
                }
            }


            Spacer(modifier = Modifier.height(dimens.medium))

            TextButton(onClick = { navController.popBackStack() }) {
                Text(
                    text = stringResource(R.string.back_to_login),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}