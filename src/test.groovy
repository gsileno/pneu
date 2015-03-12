import static groovyx.gpars.actor.Actors.*

final def decryptor = actor {
    loop {
        react { String message ->
            reply message.reverse()
        }
    }
}

def console = actor {
    decryptor.send 'lellarap si yvoorG'
    react { println 'Decrypted message: ' + it
    }
}

console.join()