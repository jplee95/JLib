package jplee.jlib.client.gui.object;

import java.util.Set;

import com.google.common.collect.Sets;

public class RadioGroup {

	Set<GuiRadioButton> radioButtons;
	
	public RadioGroup() {
		radioButtons = Sets.newHashSet();
	}
	
	public void addRadioButton(GuiRadioButton radioButton) {
		radioButton.setGroup(this);
		radioButtons.add(radioButton);
	}
	
	public void removeRadioButton(GuiRadioButton radioButton) {
		radioButton.setGroup(null);
		radioButtons.remove(radioButton);
	}
	
	public void setStates() {
		radioButtons.forEach(rb ->  {
				rb.setChecked(false);
		});
	}
}
