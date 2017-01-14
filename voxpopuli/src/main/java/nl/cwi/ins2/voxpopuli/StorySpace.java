package nl.cwi.ins2.voxpopuli;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * <p>
 * Title: Vox Populi
 * </p>
 *
 * <p>
 * Description: Automatic Generation of Biased Video Documentaries
 * </p>
 *
 * <p>
 * Copyright: Copyleft
 * </p>
 *
 * <p>
 * Company: CWI
 * </p>
 *
 * @author Stefano Bocconi
 * @version 1.0
 */
public class StorySpace {

	private final int TRANSSTEPS = 50;
	private int CurrentStep;
	private int MaxStep;

	private int S_CurrentStep;
	private int S_MaxStep;

	private boolean Saved;

	private class Snapshot {
		MyHashTable[] Character;
		ArrayList[] Plot;

		public Snapshot() {
			Character = new MyHashTable[3];
			Character[0] = new MyHashTable();
			Character[1] = new MyHashTable();
			Character[2] = new MyHashTable();

			Plot = new ArrayList[3];

			Plot[0] = new ArrayList();
			Plot[1] = new ArrayList();
			Plot[2] = new ArrayList();

		}

		public Snapshot(Snapshot a) throws Exception {
			if (a == null) {
				throw new Exception("Null in Snapshot copy constructor ");
			}

			Character = new MyHashTable[3];

			if (a.Character[0] != null) {
				Character[0] = new MyHashTable(a.Character[0]);
			}
			if (a.Character[1] != null) {
				Character[1] = new MyHashTable(a.Character[1]);
			}

			if (a.Character[2] != null) {
				Character[2] = new MyHashTable(a.Character[2]);
			}

			Plot = new ArrayList[3];

			if (a.Plot[0] != null) {
				Plot[0] = new ArrayList(a.Plot[0]);
			}
			if (a.Plot[1] != null) {
				Plot[1] = new ArrayList(a.Plot[1]);
			}
			if (a.Plot[2] != null) {
				Plot[2] = new ArrayList(a.Plot[2]);
			}

		}
	}

	private Snapshot TransSteps[];

	private Snapshot S_TransSteps[];

	public StorySpace(Hashtable<String, VideoSegment> Videos, Hashtable<String, AudioSegment> Audios,
			Hashtable<String, StillSegment> Images, Hashtable<String, TextSegment> Texts) throws Exception {

		TransSteps = new Snapshot[TRANSSTEPS];

		CurrentStep = 0;
		MaxStep = 0;

		Saved = false;

		MyHashTable Temp = new MyHashTable(Videos.size());

		if (Videos != null) {
			for (Enumeration<VideoSegment> e = Videos.elements(); e.hasMoreElements();) {
				VideoSegment theSeg = (VideoSegment) e.nextElement();
				VideoSegment aSeg = new VideoSegment(theSeg);
				Temp.Fill(aSeg);
			}
		}
		if (Audios != null) {
			for (Enumeration<AudioSegment> e = Audios.elements(); e.hasMoreElements();) {
				AudioSegment theSeg = (AudioSegment) e.nextElement();
				AudioSegment aSeg = new AudioSegment(theSeg);

				Temp.Fill(aSeg);
			}
		}

		if (Images != null) {
			for (Enumeration<StillSegment> e = Images.elements(); e.hasMoreElements();) {
				StillSegment theSeg = (StillSegment) e.nextElement();
				StillSegment aSeg = new StillSegment(theSeg);

				Temp.Fill(aSeg);
			}
		}

		if (Texts != null) {
			for (Enumeration<TextSegment> e = Texts.elements(); e.hasMoreElements();) {
				TextSegment theSeg = (TextSegment) e.nextElement();
				TextSegment aSeg = new TextSegment(theSeg);

				Temp.Fill(aSeg);
			}
		}

		TransSteps[CurrentStep] = new Snapshot();

		TransSteps[CurrentStep].Character[0] = new MyHashTable(Temp);
		TransSteps[CurrentStep].Character[1] = new MyHashTable(Temp);
		TransSteps[CurrentStep].Character[2] = new MyHashTable(Temp);

	}

	public int StartTrans() throws Exception {

		if ((CurrentStep >= TRANSSTEPS) || (CurrentStep < 0)) {
			throw new Exception("Transformation step out of range " + CurrentStep);
		}

		CurrentStep++;
		if (CurrentStep > MaxStep) {
			MaxStep++;
		}

		TransSteps[CurrentStep] = new Snapshot(TransSteps[CurrentStep - 1]);

		return CurrentStep - 1;
	}

	public void EndTrans() throws Exception {

	}

	public MyHashTable GetCharacter(int i) {

		return TransSteps[CurrentStep].Character[i];
	}

	public ArrayList GetLastPlot() throws Exception {

		int size0 = TransSteps[CurrentStep].Plot[0].size();
		int size1 = TransSteps[CurrentStep].Plot[1].size();
		int size2 = TransSteps[CurrentStep].Plot[2].size();

		if (size0 > 0 || size1 > 0 || size2 > 0) {
		} else {
			throw new Exception("No media item selected ");
		}

		ArrayList Plot0 = TransSteps[CurrentStep].Plot[0];
		ArrayList Plot1 = TransSteps[CurrentStep].Plot[1];
		ArrayList Plot2 = TransSteps[CurrentStep].Plot[2];

		ArrayList Plot = new ArrayList();

		int maxlength = Util.max(size0, size1, size2);

		for (int i = 0; i < maxlength; i++) {
			if (i < size0) {
				Plot.add(Plot0.get(i));
			}
			if (i < size1) {
				Plot.add(Plot1.get(i));
			}
			if (i < size2) {
				Plot.add(Plot2.get(i));
			}
		}

		return Plot;

	}

	public ArrayList GetPlot(int i) {

		return TransSteps[CurrentStep].Plot[i];
	}

	public int SizePlot(int i) {

		return TransSteps[CurrentStep].Plot[i].size();
	}

	public boolean SetTrans(int i) throws Exception {

		if ((i >= TRANSSTEPS) || (i < 0) || (i > MaxStep)) {
			return false;
		}

		CurrentStep = i;

		return true;

	}

	public int GetTrans() throws Exception {

		return CurrentStep;

	}

	public void SaveSpace() throws Exception {

		S_CurrentStep = CurrentStep;
		S_MaxStep = MaxStep;

		S_TransSteps = new Snapshot[TRANSSTEPS];

		for (int i = 0; i <= MaxStep; i++) {
			S_TransSteps[i] = new Snapshot(TransSteps[i]);
		}

		Saved = true;
	}

	public boolean RestoreSpace() throws Exception {

		if (Saved == false) {
			return false;
		}
		CurrentStep = S_CurrentStep;
		MaxStep = S_MaxStep;

		TransSteps = new Snapshot[TRANSSTEPS];

		for (int i = 0; i <= MaxStep; i++) {
			TransSteps[i] = new Snapshot(S_TransSteps[i]);
		}
		Saved = false;
		return true;

	}

	public boolean AddUniquetoPlot(MediaItem a, int i) throws Exception {

		if (a.VideoSegments.size() > 0 || a.AudioSegments.size() > 0 || a.StillSegments.size() > 0
				|| a.TextSegments.size() > 0) {

			MediaItem Add = new MediaItem();

			int size = a.VideoSegments.size();

			for (int j = 0; j < size; j++) {
				Segment theSeg = (Segment) a.VideoSegments.get(j);
				if (this.PlotContains(theSeg, i) == -1) {
					Add.add(theSeg);
				}
			}
			size = a.AudioSegments.size();

			for (int j = 0; j < size; j++) {
				Segment theSeg = (Segment) a.AudioSegments.get(j);
				if (this.PlotContains(theSeg, i) == -1) {
					Add.add(theSeg);
				}
			}
			size = a.StillSegments.size();

			for (int j = 0; j < size; j++) {
				Segment theSeg = (Segment) a.StillSegments.get(j);
				if (this.PlotContains(theSeg, i) == -1) {
					Add.add(theSeg);
				}
			}
			size = a.TextSegments.size();

			for (int j = 0; j < size; j++) {
				Segment theSeg = (Segment) a.TextSegments.get(j);
				if (this.PlotContains(theSeg, i) == -1) {
					Add.add(theSeg);
				}
			}

			TransSteps[CurrentStep].Plot[i].add(Add);

			return true;
		}
		return false;
	}

	public boolean AddtoPlot(MediaItem a, int i) throws Exception {

		if (a.VideoSegments.size() > 0 || a.AudioSegments.size() > 0 || a.StillSegments.size() > 0
				|| a.TextSegments.size() > 0) {

			TransSteps[CurrentStep].Plot[i].add(a);

			return true;
		}
		return false;
	}

	public int PlotContains(Segment theSeg, int index) throws Exception {

		int size = TransSteps[CurrentStep].Plot[index].size();

		for (int i = 0; i < size; i++) {
			MediaItem item = (MediaItem) TransSteps[CurrentStep].Plot[index].get(i);
			if (item.Contains(theSeg)) {
				return i;
			}
		}
		return -1;
	}

	public boolean StoryContains(String theItem, Hashtable Story) throws Exception {

		return false;

	}

	public void ExpungeFromPlot(MediaItem item) throws Exception {

		int index;

		if ((index = ExpungeFromPlot(item, 1)) != -1) {
			if (ExpungeFromPlot(index, 2) == -1) {
				// Do nothing because we might have only one plot
			}

		} else if ((index = ExpungeFromPlot(item, 2)) != -1) {
			if (ExpungeFromPlot(index, 1) == -1) {
				throw new Exception(" Element not found in parallel plot ");
			}
		}
		return;

	}

	public int ExpungeFromPlot(MediaItem item, int index) {

		int pointer = -1;

		if (item == null || (pointer = TransSteps[CurrentStep].Plot[index].lastIndexOf(item)) == -1) {
			return -1;
		}

		TransSteps[CurrentStep].Plot[index].remove(pointer);

		return pointer;
	}

	public int ExpungeFromPlot(int index, int which) {

		MediaItem item;

		if ((item = (MediaItem) TransSteps[CurrentStep].Plot[which].get(index)) == null) {
			return -1;
		}

		TransSteps[CurrentStep].Plot[which].remove(index);
		return index;
	}

}
