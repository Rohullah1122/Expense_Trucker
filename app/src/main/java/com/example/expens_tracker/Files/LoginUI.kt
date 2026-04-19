package com.example.expens_tracker.Files

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expens_tracker.Classes.Routes
import com.example.expens_tracker.R
import com.example.expens_tracker.ViewModel.LoginState
import com.example.expens_tracker.ViewModel.LoginViewModel
import com.example.expens_tracker.ui.theme.AppTheme // Import your AppTheme object

@Composable
fun LoginUI(navController: NavController) {

    val viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val loginState by viewModel.loginState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }








    // Accessing our custom dimensions
    val dimens = AppTheme.dimens

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = dimens.extraLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Replaced Logo size with Dimens
            Box(
                modifier = Modifier
                    .size(dimens.logoSize)
                    .background(
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                        RoundedCornerShape(dimens.roundedCorner)
                    )
                    .border(dimens.glassBorder, MaterialTheme.colorScheme.outline, RoundedCornerShape(dimens.roundedCorner)),
                contentAlignment = Alignment.Center
            ) {
                Text("RH", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(dimens.large))

            Text(
                text = stringResource(R.string.welcome_back),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = stringResource(R.string.sign_in_subtitle),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(dimens.extraLarge))

            // Glass Email Field
            GlassInputField(
                value = email,
                onValueChange = { email = it },
                label = stringResource(R.string.email_label),
                placeholder = stringResource(R.string.email_placeholder),
                icon = Icons.Default.Email
            )

            Spacer(modifier = Modifier.height(dimens.medium))

            // Glass Password Field
            GlassInputField(
                value = password,
                onValueChange = { password = it },
                label = stringResource(R.string.password_label),
                placeholder = stringResource(R.string.password_placeholder),
                icon = Icons.Default.Lock,
                isPassword = true,
                passwordVisible = passwordVisible,
                onVisibilityChange = { passwordVisible = !passwordVisible }
            )

            Spacer(modifier = Modifier.height(dimens.small))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = {

                    navController.navigate(Routes.FORGOT_PASSWORD)

                }) {
                    Text(
                        text = stringResource(R.string.forgot_password),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimens.large))

            // Button using Theme Colors and Dimens
            Button(
                onClick = { viewModel.login(email, password) },   // ⭐ CHANGED: now calls ViewModel
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.buttonHeight)
                    .clip(RoundedCornerShape(dimens.roundedCorner))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        )
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(
                    text = stringResource(R.string.sign_in_button),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }


            LaunchedEffect(loginState) {
                when (loginState) {
                    is LoginState.Success -> navController.navigate(Routes.MainLayout)
                    is LoginState.Error -> {
                        // TODO: show snackbar or toast
                    }
                    else -> {}
                }
            }


            TextButton(onClick = { navController.navigate(Routes.REGISTER) }) {
                Text(
                    text = stringResource(R.string.register_link),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun GlassInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onVisibilityChange: () -> Unit = {}
) {
    val dimens = AppTheme.dimens

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = MaterialTheme.typography.labelMedium.fontSize,
            modifier = Modifier.padding(bottom = dimens.small)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimens.inputHeight)
                .background(
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                    RoundedCornerShape(dimens.roundedCorner)
                )
                .border(dimens.glassBorder, MaterialTheme.colorScheme.outline, RoundedCornerShape(dimens.roundedCorner))
                .padding(horizontal = dimens.medium),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(dimens.small))

                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                    }
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        singleLine = true,
                        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (isPassword) {
                    IconButton(onClick = onVisibilityChange) {
                        Icon(
                            if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}