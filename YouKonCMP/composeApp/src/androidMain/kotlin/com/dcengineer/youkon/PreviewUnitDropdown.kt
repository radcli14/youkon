package com.dcengineer.youkon

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import model.YkUnit
import view.UnitDropdown

@Preview
@Composable
fun UnitDropdownPreview() {
    UnitDropdown(YkUnit.METERS, YkUnit.METERS.allUnits, onShowPaywall = {}).Body()
}
