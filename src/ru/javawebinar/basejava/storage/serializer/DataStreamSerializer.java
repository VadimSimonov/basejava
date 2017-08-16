package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            // TODO implements contacts
            Map<ContactType, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }
            // TODO implements sections
            Map<SectionType,Section>sectional = r.getSections();
            dos.writeInt(sectional.size());
            for (Map.Entry<SectionType,Section> entry:sectional.entrySet())
            {
                SectionType sectionType=entry.getKey();
                Section section=entry.getValue();
                if (sectionType==SectionType.PERSONAL || sectionType==SectionType.OBJECTIVE) {
                    dos.writeUTF(sectionType.name());
                    dos.writeUTF(((TextSection)section).getContent());
                }

                else if (sectionType==SectionType.ACHIEVEMENT || sectionType==SectionType.QUALIFICATIONS) {
                  //  int size=(((ListSection)section).getItems()).size();
                    List<String> list = (((ListSection) section).getItems());
                    dos.writeInt(list.size());
                    System.out.println("sizeList="+list.size());
                    dos.writeUTF(sectionType.name());
                    System.out.println("nameKey="+sectionType.name());
                    for (int i = 0; i <list.size() ; i++) {
                        dos.writeUTF(list.get(i));
                        System.out.println("wtiteData="+list.get(i));
                    }
                } else if (sectionType==SectionType.EXPERIENCE || sectionType==SectionType.EDUCATION) {
                    List<Organization> list = (((OrganizationSection) section).getOrganizations());
                    dos.writeUTF(sectionType.name());
                    for (Organization organization : list) {
                        dos.writeUTF(organization.getHomePage().getName());
                        String str = organization.getHomePage().getUrl();
                        if (str==null){
                            dos.writeUTF("");
                        }else
                            dos.writeUTF(str);

                        List<Organization.Position> positionList = organization.getPositions();
                        for (Organization.Position aPositionList : positionList) {
                            dos.writeInt(aPositionList.getStartDate().getYear());
                            dos.writeInt(aPositionList.getStartDate().getMonth().getValue());
                            dos.writeInt(aPositionList.getEndDate().getYear());
                            dos.writeInt(aPositionList.getEndDate().getMonth().getValue());
                            dos.writeUTF(aPositionList.getTitle());
                            String str1=aPositionList.getDescription();
                            if (str1==null){
                                dos.writeUTF("");
                            }else
                            dos.writeUTF(aPositionList.getDescription());
                        }
                    }
                }
            }
        }
    }


    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            // TODO implements contacts
            Resume resume = new Resume(uuid, fullName);
            int size=dis.readInt();
            String string="";
            String string1="";
            String string2="";
            for (int i = 0; i < 8; i++) {
                string1=dis.readUTF();
                string2=dis.readUTF();
                resume.addContact(ContactType.valueOf(string1), string2);
        //        string=string+" "+string1+" "+string2;

            }
           // System.out.println(string);

            int siz=dis.readInt();
            String str="";
            String str1="";
            for (int i = 0; i <siz; i++) {
                        str1=dis.readUTF();
                        str=string+" "+str1;
            }
            System.out.println(str);

            // TODO implements sections
            //int siz=dis.readInt();
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                if (sectionType.equals(SectionType.PERSONAL) || sectionType.equals(SectionType.OBJECTIVE)) {
                    resume.addSection(sectionType, new TextSection(dis.readUTF()));

                } else if (sectionType.equals(SectionType.ACHIEVEMENT) || sectionType.equals(SectionType.QUALIFICATIONS)) {
                    List<String>list=ReadList(dis);
                    resume.addSection(sectionType, new ListSection(list));

                } else if (sectionType.equals(SectionType.EXPERIENCE) || sectionType.equals(SectionType.EDUCATION)) {

                }


            return resume;
        }
    }

    private List<String> ReadList(DataInputStream dis) throws IOException {
        int size=dis.readInt();
        List<String> list=new ArrayList<>();
        for (int j = 0; j <size ; j++) {
            list.add(dis.readUTF());
        }
        return list;
    }
}
