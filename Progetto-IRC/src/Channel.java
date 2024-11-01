import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

public class Channel {
	private final String nomeChannel;
	private ArrayList<ThreadCommunication> clientConnectionList;
	

	public Channel(String nome) {
		super();
		this.clientConnectionList = new ArrayList<>();
		this.nomeChannel = nome;
	}

	public String getNomeChannel() { return nomeChannel;}

	public void addClient(Socket clientSocket) {
		clientConnectionList.add(new ThreadCommunication(this, clientSocket));
	}

	public void inoltro(Pacchetto pacchetto, long threadMittenteId) {
		clientConnectionList.forEach((thread) -> {
			if (thread.threadId()!=threadMittenteId) {
				thread.invia(pacchetto);
			}
		});
	}

	public void whisper(String clientReceiver, Pacchetto pacchetto){
		clientConnectionList.forEach((thread) -> {
			if (thread.getClientName().equals(clientReceiver)) {
				thread.invia(pacchetto);
			}
		});
	}

	public boolean isNomeClientOK(ThreadCommunication caller, String requestedName) {
		if(requestedName==null || requestedName.isEmpty() || requestedName.matches("^[^a-zA-Z0-9_]*$") || requestedName.startsWith("Client")) {
			return false;
		} else {
			for (ThreadCommunication thread : clientConnectionList) {
				if(thread.threadId()!=caller.threadId() && thread.getClientName().equals(requestedName)) {
					return false;
				}
			}
		}
		return true;
	}

	public String generaNomeClient() {
		//genero una stringa alfanumerica casuale
		return UUID.randomUUID().toString().replaceAll("_", "").substring(0,5);
	}

	public void chiudiSocket(ThreadCommunication threadToClose) {
		clientConnectionList.remove(threadToClose);
		System.out.println("Rimuovo un client dal channel.");
	}
}
