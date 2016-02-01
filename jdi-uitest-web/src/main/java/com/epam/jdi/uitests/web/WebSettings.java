/*
 * Copyright 2004-2016 EPAM Systems
 *
 * This file is part of JDI project.
 *
 * JDI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JDI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty ofMERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JDI. If not, see <http://www.gnu.org/licenses/>.
 */

package com.epam.jdi.uitests.web;

import com.epam.jdi.uitests.core.interfaces.base.IClickable;
import com.epam.jdi.uitests.core.interfaces.base.IElement;
import com.epam.jdi.uitests.core.interfaces.common.*;
import com.epam.jdi.uitests.core.interfaces.complex.*;
import com.epam.jdi.uitests.core.settings.JDISettings;
import com.epam.jdi.uitests.web.selenium.driver.DriverTypes;
import com.epam.jdi.uitests.web.selenium.driver.ScreenshotMaker;
import com.epam.jdi.uitests.web.selenium.driver.SeleniumDriverFactory;
import com.epam.jdi.uitests.web.selenium.elements.MapInterfaceToElement;
import com.epam.jdi.uitests.web.selenium.elements.base.Clickable;
import com.epam.jdi.uitests.web.selenium.elements.base.Element;
import com.epam.jdi.uitests.web.selenium.elements.common.*;
import com.epam.jdi.uitests.web.selenium.elements.complex.*;
import com.epam.jdi.uitests.web.selenium.elements.complex.table.Table;
import com.epam.jdi.uitests.web.selenium.elements.complex.table.interfaces.ITable;
import com.epam.jdi.uitests.web.testng.testRunner.TestNGLogger;
import com.epam.web.matcher.testng.Check;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.function.Supplier;

import static com.epam.commons.PropertyReader.fillAction;
import static com.epam.commons.PropertyReader.getProperties;
import static com.epam.web.matcher.base.DoScreen.SCREEN_ON_FAIL;

/**
 * Created by Roman_Iovlev on 11/13/2015.
 */
public class WebSettings extends JDISettings {
    public static WebDriver getDriver() {
        return getDriverFactory().getDriver();
    }

    public static SeleniumDriverFactory getDriverFactory() {
        return (SeleniumDriverFactory) JDISettings.driverFactory;
    }

    public static String useDriver(DriverTypes driverName) {
        return getDriverFactory().registerDriver(driverName);
    }

    public static String useDriver(Supplier<WebDriver> driver) {
        return getDriverFactory().registerDriver(driver);
    }

    public static JavascriptExecutor getJSExecutor() {
        if (driverFactory.getDriver() instanceof JavascriptExecutor) {
            return (JavascriptExecutor) driverFactory.getDriver();
        } else {
            throw new ClassCastException("JavaScript Executor doesn't support");
        }
    }

    public synchronized static void init() throws IOException {
        driverFactory = new SeleniumDriverFactory();
        asserter = new Check() {
            @Override
            protected String doScreenshotGetMessage() {
                return ScreenshotMaker.doScreenshotGetMessage();
            }
        }.doScreenshot(SCREEN_ON_FAIL);
        timeouts = new WebTimeoutSettings();
        getProperties(jdiSettingsPath);
        fillAction(p -> logger = (p.equals("true") || p.equals("1"))
                ? new TestNGLogger("JDI Logger", s -> String.format("[ThreadId: %s] %s", Thread.currentThread().getId(), s))
                : new TestNGLogger("JDI Logger")
                , "multithread");
        MapInterfaceToElement.init(defaultInterfacesMap);
    }

    private static Object[][] defaultInterfacesMap = new Object[][]{
            {IElement.class, Element.class},
            {IButton.class, Button.class},
            {IClickable.class, Clickable.class},
            {IComboBox.class, ComboBox.class},
            {ILink.class, Link.class},
            {ISelector.class, Selector.class},
            {IText.class, Text.class},
            {IImage.class, Image.class},
            {ITextArea.class, TextArea.class},
            {ITextField.class, TextField.class},
            {ILabel.class, Label.class},
            {IDropDown.class, Dropdown.class},
            {IDropList.class, DropList.class},
            {IGroup.class, ElementsGroup.class},
            {ITable.class, Table.class},
            {ICheckBox.class, CheckBox.class},
            {IRadioButtons.class, RadioButtons.class},
            {ICheckList.class, CheckList.class},
            {ITextList.class, TextList.class},
            {ITabs.class, Tabs.class},
            {IMenu.class, Menu.class},
            {IFileInput.class, FileInput.class},
            {IDatePicker.class, DatePicker.class},
    };
}