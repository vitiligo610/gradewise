package com.vitiligo.gradewise.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BottomOutlineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    dividerColor: Color = DividerDefaults.color,
    enabled: Boolean = true,
    space: Dp = 8.dp,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    var dividerColor1 by remember { mutableStateOf(dividerColor) }

    Column(
        verticalArrangement = Arrangement.spacedBy(space),
        modifier = modifier
            .fillMaxWidth()
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = textStyle,
            decorationBox = { innerTextField ->
                innerTextField()
            },
            enabled = enabled,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
            singleLine = true,
            maxLines = 1,
            keyboardOptions = keyboardOptions,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    dividerColor1 = if (it.isFocused)
                        primaryColor
                    else
                        dividerColor
                }
                .then(
                    if (focusRequester != null)
                        Modifier.focusRequester(focusRequester)
                    else
                        Modifier
                )
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = if (!enabled) Color.Transparent else dividerColor1,
            modifier = Modifier
                .animateContentSize()
        )
    }
}