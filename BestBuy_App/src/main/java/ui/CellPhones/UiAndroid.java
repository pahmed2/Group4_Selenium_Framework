package ui.CellPhones;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ui.GlobalHeader.ShopMenu.UiCellPhones;

public class UiAndroid extends UiCellPhones{
    @FindBy(css = ".category-headline")
    public WebElement pageHeading;
    public String textPageHeading = "Android";
    public void assertPage(){
        Assert.assertTrue(textPageHeading.equals(pageHeading.getText()));
    }
}
