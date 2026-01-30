import { useState } from 'react'

function BackSide({answer}) {
    return (
        <div>
            <h2>{answer}</h2>
        </div>
    );
}


function FrontSide({question}) {
    return (
        <div>
            <h2>{question}</h2>
        </div>
    );
}

export default function FlashCard({ question, answer }) {
    const [isFlipped, setFlipped] = useState(false);

    return (
        <div onClick={() => setFlipped(false)}>
            {isFlipped ? (
                <BackSide answer={answer} />
            ) : (
                <FrontSide question={question} />
            )}
        </div>
    );
}

