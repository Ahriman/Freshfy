package com.marcossan.freshfy.views.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ScopeUpdateScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.marcossan.freshfy.R
import com.marcossan.freshfy.data.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Scope


@Composable
fun ProductQuantity(
    product: Product,
    onProductQuantityChange: (String) -> Unit,
    scope: CoroutineScope
) {

//    val scope = rememberCoroutineScope()
    var intQuantity by remember { mutableIntStateOf(product.quantity.toInt()) }

    Row(verticalAlignment = Alignment.CenterVertically) {

        Button(onClick = {
            if (intQuantity in 2..999) {
                intQuantity--
            }
        }) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Añadir cantidad"
            )
        }
        Box(
            modifier = Modifier.padding(8.dp)
        ) {
            TextField(
                value = intQuantity.toString(),
                onValueChange = {
                    // Actualizar el conteo cuando el usuario modifica el TextField
                    val newValue = it.toIntOrNull() ?: 0
                    if (newValue in 1..999) {
                        intQuantity = newValue

                        scope.launch {
                            onProductQuantityChange(newValue.toString())
                        }
                    }
                },
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
//                visualTransformation = numberInputFilter(maxValue = 999),
                visualTransformation = VisualTransformation.None,
                label = {
                    Text(
                        text = stringResource(R.string.product_quantity),
                        textAlign = TextAlign.Center,
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .width(90.dp)
                    .background(color = Color.Transparent)
//                    .border(
//                        0.dp,
//                        Color.Gray,
//                        shape = MaterialTheme.shapes.small
//                    ) // Borde personalizado

            )
        }
        Button(onClick = {
            if (intQuantity in 1..998) {
                intQuantity++
            }
        }) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Añadir cantidad"
            )
        }
    }
}

