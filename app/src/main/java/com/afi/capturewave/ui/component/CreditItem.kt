package com.afi.capturewave.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.afi.capturewave.ui.items.Credit

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreditItem(
    credit: Credit,
    typography: Typography = MaterialTheme.typography,
    onClick: () -> Unit,
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val badgeBackgroundColor = MaterialTheme.colorScheme.primary
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable { onClick.invoke() }
            .padding(PaddingValues(16.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = credit.name,
                modifier = Modifier
                    .padding(PaddingValues(top = 4.dp))
                    .weight(1f),
                style = typography.titleLarge,
                color = contentColorFor(backgroundColor),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = credit.version,
                modifier = Modifier.padding(PaddingValues(start = 8.dp)),
                style = typography.bodyMedium,
                color = contentColorFor(backgroundColor),
                textAlign = TextAlign.Center
            )

        }
        Text(
            text = credit.author,
            style = typography.bodyMedium,
            color = contentColorFor(backgroundColor)
        )

        FlowRow {
            Badge(
                modifier = Modifier.padding(
                    PaddingValues(
                        top = 8.dp,
                        end = 4.dp
                    )
                ),
                contentColor = contentColorFor(badgeBackgroundColor),
                containerColor = badgeBackgroundColor
            ) {
                Text(
                    modifier = Modifier.padding(PaddingValues(0.dp)),
                    text = credit.license
                )
            }
        }
    }
}