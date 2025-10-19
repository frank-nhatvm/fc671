package com.fatherofapps.fc671

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

import fc671.composeapp.generated.resources.Res
import fc671.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun App() {
    MaterialTheme {

        var isSignedInAsHost by remember { mutableStateOf(false) }

        var isSigningInAsHost by remember { mutableStateOf(false) }

        var hostName by remember { mutableStateOf("") }
        var hostPassword by remember { mutableStateOf("") }
        Box(
            modifier = Modifier
                .fillMaxSize(), // Full height & width
            contentAlignment = Alignment.TopCenter // Center horizontally for large screens
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth().widthIn(max = 420.dp).background(MaterialTheme.colorScheme.primaryContainer)
                    .safeContentPadding().verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start,
            ) {

                Text("Chủ xị có quyền tạo danh sách người chơi, chọn đội trưởng và điều phối việc chọn team.")
                Spacer(modifier = Modifier.height(12.dp))
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        if (isSignedInAsHost) {
                            Text("Bạn đang là chủ xị")
                        }
                        val textButton =
                            if (isSigningInAsHost) {
                                "Huỷ"
                            } else if (isSignedInAsHost) {
                                "Đăng xuất"
                            } else {
                                "Đăng nhập để trở thành chủ xị"
                            }
                        Button(onClick = {
                            if (isSigningInAsHost) {
                                // huy dang nhap
                                isSigningInAsHost = false
                            } else if (isSigningInAsHost) {
                                // dang xuat
                                isSigningInAsHost = false
                            } else {
                                isSigningInAsHost = true
                            }
                        }) {
                            Text(textButton)
                        }
                    }

                    if (isSigningInAsHost) {
                        Column(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
                            OutlinedTextField(
                                value = hostName, onValueChange = { hostName = it }, modifier = Modifier.fillMaxWidth(),
                                label = { Text("Hostname") },
                                singleLine = true,
                            )
                            OutlinedTextField(
                                value = hostPassword,
                                onValueChange = { hostPassword = it },
                                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                                label = { Text("Password") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                visualTransformation = PasswordVisualTransformation()
                            )
                            Button(
                                onClick = {
                                    if (hostName == "admin" && hostPassword == "fc671admin@2025") {
                                        isSigningInAsHost = false
                                        isSignedInAsHost = true
                                        hostName = ""
                                        hostPassword = ""
                                    }
                                },
                                enabled = hostName.isNotEmpty() && hostPassword.isNotEmpty(),
                            ) {
                                Text("Dang nhap")
                            }
                        }
                    }


                }

            }
        }
    }
}