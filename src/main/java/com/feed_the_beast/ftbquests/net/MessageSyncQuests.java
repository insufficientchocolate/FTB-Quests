package com.feed_the_beast.ftbquests.net;

import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import com.feed_the_beast.ftbquests.gui.ClientQuestList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
public class MessageSyncQuests extends MessageToClient
{
	public NBTTagCompound quests;
	public String team;
	public NBTTagCompound taskData;
	public int[] claimedRewards;
	public boolean editingMode;

	public MessageSyncQuests()
	{
	}

	public MessageSyncQuests(NBTTagCompound n, String t, NBTTagCompound td, int[] r, boolean e)
	{
		quests = n;
		team = t;
		taskData = td;
		claimedRewards = r;
		editingMode = e;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return FTBQuestsNetHandler.GENERAL;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeNBT(quests);
		data.writeString(team);
		data.writeNBT(taskData);
		data.writeShort(claimedRewards.length);

		for (int i : claimedRewards)
		{
			data.writeShort(i);
		}

		data.writeBoolean(editingMode);
	}

	@Override
	public void readData(DataIn data)
	{
		quests = data.readNBT();
		team = data.readString();
		taskData = data.readNBT();
		claimedRewards = new int[data.readUnsignedShort()];

		for (int i = 0; i < claimedRewards.length; i++)
		{
			claimedRewards[i] = data.readUnsignedShort();
		}

		editingMode = data.readBoolean();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onMessage()
	{
		if (ClientQuestList.INSTANCE != null)
		{
			ClientQuestList.INSTANCE.invalidate();
		}

		ClientQuestList.INSTANCE = new ClientQuestList(this, ClientQuestList.INSTANCE);
	}
}