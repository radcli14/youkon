//
//  CustomDecimalTextField.swift
//  iosApp
//
//  Created by Eliott Radcliffe on 7/24/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import UIKit

@objc public class CustomAppleDecimalTextField: UITextField {

    public var toolbar: UIToolbar?

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setupKeyboard()
    }

    override init(frame: CGRect) {
        super.init(frame: frame)
        setupKeyboard()
    }

    private func setupKeyboard() {
        let negativeButton = UIBarButtonItem(title: "-", style: .plain, target: self, action: #selector(insertNegativeSign))
        let flexibleSpace = UIBarButtonItem(barButtonSystemItem: .flexibleSpace, target: nil, action: nil)
        let doneButton = UIBarButtonItem(title: "Done", style: .done, target: self, action: #selector(done))

        toolbar = UIToolbar()
        toolbar?.items = [negativeButton, flexibleSpace, doneButton]
        toolbar?.sizeToFit()

        self.inputAccessoryView = toolbar
        self.keyboardType = .decimalPad
    }

    @objc func insertNegativeSign() {
        let currentText = self.text ?? ""
        if !currentText.starts(with: "-") {
            self.text = "-\(currentText)"
        }
    }

    @objc func done() {
        self.resignFirstResponder()
    }
}
