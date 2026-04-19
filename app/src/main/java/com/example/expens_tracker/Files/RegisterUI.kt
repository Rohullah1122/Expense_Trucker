package com.example.expens_tracker.Files

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.expens_tracker.R
import com.example.expens_tracker.ViewModel.RegisterState
import com.example.expens_tracker.ViewModel.RegisterViewModel
import com.example.expens_tracker.ui.theme.AppTheme

@Composable
fun RegisterUI(navController: NavController) {

    val viewModel: RegisterViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val registerState by viewModel.registerState.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val dimens = AppTheme.dimens

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Decorative Background Glow - Uses primary color with transparency
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 100.dp, y = 100.dp)
                .blur(100.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    shape = CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = dimens.extraLarge, vertical = dimens.extraLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.create_account),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(dimens.extraLarge))

            // Full Name Input
            RegisterGlassInput(
                value = fullName,
                onValueChange = { fullName = it },
                label = stringResource(R.string.full_name_label),
                placeholder = stringResource(R.string.full_name_placeholder),
                icon = Icons.Default.Person
            )

            Spacer(modifier = Modifier.height(dimens.medium))

            // Email Input
            RegisterGlassInput(
                value = email,
                onValueChange = { email = it },
                label = stringResource(R.string.email_label),
                placeholder = stringResource(R.string.email_placeholder),
                icon = Icons.Default.Email
            )

            Spacer(modifier = Modifier.height(dimens.medium))

            // Password Input
            RegisterGlassInput(
                value = password,
                onValueChange = { password = it },
                label = stringResource(R.string.password_label),
                placeholder = stringResource(R.string.password_placeholder),
                icon = Icons.Default.Lock,
                isPassword = true,
                passwordVisible = passwordVisible,
                onVisibilityChange = { passwordVisible = !passwordVisible }
            )

            Spacer(modifier = Modifier.height(dimens.medium))

            // Confirm Password Input (Using the same strings/labels logic)
            RegisterGlassInput(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = stringResource(R.string.confirm_password_label),
                placeholder = stringResource(R.string.password_placeholder),
                icon = Icons.Default.Lock,
                isPassword = true,
                passwordVisible = passwordVisible,
                onVisibilityChange = { passwordVisible = !passwordVisible }
            )

            Spacer(modifier = Modifier.height(dimens.extraLarge))

            // Gradient Sign Up Button
            Button(
                onClick = {
                    viewModel.register(fullName, email, password)
                }
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
                    text = stringResource(R.string.sign_up_button),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            LaunchedEffect(registerState) {
                when (registerState) {
                    is RegisterState.Success -> {
                        navController.popBackStack() // go back to login
                    }
                    is RegisterState.Error -> {
                        // TODO: show snackbar or toast
                    }
                    else -> {}
                }
            }




            Spacer(modifier = Modifier.height(dimens.medium))

            TextButton(onClick = { navController.popBackStack() }) {
                Text(
                    text = stringResource(R.string.already_have_account),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun RegisterGlassInput(
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
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(dimens.roundedCorner)
                )
                .border(
                    width = dimens.glassBorder,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(dimens.roundedCorner)
                )
                .padding(horizontal = dimens.medium),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(dimens.small))
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize
                        )
                    }
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        singleLine = true,
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (isPassword) {
                    IconButton(onClick = onVisibilityChange) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}