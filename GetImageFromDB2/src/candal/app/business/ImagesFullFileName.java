package candal.app.business;

public class ImagesFullFileName {

	private String[] _imagesFullFileName = null;
    private int _maxItems = 3;
    
	public ImagesFullFileName() {
		
		_imagesFullFileName = new String[_maxItems];
		
		for (int i = 0; i < _maxItems; i++)
			_imagesFullFileName[i] = "";
	}
	
	public void setImageFullFileName(int index, String ImageFullFileName) {
		
		if ((index < 0) || (index > (_maxItems - 1)))
			return;
		
		_imagesFullFileName[index] = ImageFullFileName;
	}

	public String getImageFullFileName(int index) {
		
		if ((index < 0) || (index > (_maxItems - 1)))
			return "";
		
		return _imagesFullFileName[index];
	}
}
