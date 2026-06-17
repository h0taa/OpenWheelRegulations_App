package com.openwheel.regulations.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.openwheel.regulations.R

/**
 * The app's logo: a flat-design open-wheel car on a navy rounded badge, shown from the
 * bundled image asset (res/drawable-nodpi/ic_owr_logo.png) so the splash and About screens
 * match the launcher icon exactly. The badge already has rounded corners with a transparent
 * surround, so it drops cleanly onto any background.
 *
 * Pass a size via [modifier] (e.g. Modifier.size(132.dp)); the badge fills it.
 */
@Composable
fun OwrLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_owr_logo),
        contentDescription = null,
        modifier = modifier
    )
}
