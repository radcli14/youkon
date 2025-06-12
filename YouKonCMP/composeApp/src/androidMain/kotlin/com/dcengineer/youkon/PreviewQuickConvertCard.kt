package com.dcengineer.youkon

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import model.YkUnit
import view.QuickConvertCard
import viewmodel.QuickConvertCardViewModel


@Preview
@Composable
fun QuickConvertCardPreview() {
    val vm = QuickConvertCardViewModel()
    vm.updateUnit(YkUnit.KILOGRAMS_PER_METER_CUBED)
    QuickConvertCard(vm).Body()
}
