package cn.hmxin.readbookproject;
import java.io.File;

public class SubFile {
	
	private File file =null ;//��װһ��File�࣬���ڽ��մ������췽���е�File��
	
	public SubFile(File file) {//���췽��
		this.file = file ;//�����е�file��ֵΪ��������file
	}
	
	public File getFile() {//getter����������File��
		return this.file;//���ر����е�file����
	}
	
	@Override
	public String toString() {
		String str = null; //����һ���ַ�����������ֵ
		if(file.isDirectory()) {//���ļ���
			str = "[�ļ���]" + file.getName();
		}else {//���ļ�
			if(file.getName().lastIndexOf(".txt")>-1) {//�����ǹ��˵�����ļ������ļ������������ȥ��
				str = "[�ļ�]" + file.getName();//��ֵ"[�ļ�]"+�ļ�����
			}
		}
		return str;//���ظ��ַ���
	}
}
