package ch.nutrio.ui.components;

import java.util.List;

import ch.nutrio.data.PersonalData;
import ch.nutrio.data.StoreKeeper;
import com.vaadin.componentfactory.Chat;
import com.vaadin.componentfactory.model.Message;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.Scroller;

public class ChatComponent {

  public static Component create() {
    final PersonalData personalData = StoreKeeper.getStoreForSession().getPersonalData();
    final String firstname = personalData.getFirstname();
    final String lastname = personalData.getName();
    final String fullname = firstname + " " + lastname;
    Chat chat = new Chat();
    chat.setMessages(startMessages(fullname));
    chat.setDebouncePeriod(200);
    chat.setLazyLoadTriggerOffset(2500);
    chat.scrollToBottom();

    chat.addChatNewMessageListener(event -> {
      event.getSource().addNewMessage(new Message(event.getMessage(),
                                                  "",
                                                  fullname, true));
      event.getSource().clearInput();
      event.getSource().scrollToBottom();
    });

    chat.setLoadingIndicator(createLoadingComponent());

    final Scroller scroller = new Scroller();
    scroller.setContent(chat);
    scroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
    scroller.setSizeFull();

    return scroller;
  }

  private static Component createLoadingComponent() {
    Div loading = new Div();
    loading.setText("Laden...");
    loading.getElement().setAttribute("style", "text-align: center;");
    return loading;
  }

  private static List<Message> startMessages(final String fullname) {
    final String wokerName = "Eveline Bauer";
    return List.of(
        new Message("Hallo " + fullname + ", Sie haben schon länger das Gewicht nicht mehr erfasst.", "", wokerName, false));
  }
}
