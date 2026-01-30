import { useState, useEffect, useCallBack } from 'react';
import FlashCard from '../components/FlashCard';
import { useParams } from 'react-router-dom';




export default function CramMode() {
    const { type, id} = useParams();
    const [cards,setCards] = useState([]);
    const [currentIndex, setCurrentIndex] = useState(0);

    const fetchMoreCardsUrl = `http://localhost:8080/api/cram/${type}/${id}/batch`;
    const submitScoreUrl = "";

    const fetchMoreCards = useCallBack(() => {
        fetch(fetchMoreCardsUrl)
        .then((response) => {
            return response.json();
        })
        .then((newCards) => {
            setCards((prevDeck) => [...prevDeck, ...newCards]);

        })
        .catch((error) => {
            console.error('Error fetching more cards:', error);
        });
    });

    function newCard() {
        if(currentIndex % 5 == 3) {
            fetchMoreCards();
        }
        setCurrentIndex(currentIndex+1);

    }

    const submitScore = (score) => {
        fetch(`${submitScoreUrl}/${cardId}/score`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ score: score })
        })
        .then(response => {
            if (!response.ok) console.error("Database didn't update the score");
        })
        .catch(err => console.error("Connection error", err));

        newCard();
    }

    useEffect(() => {
        fetchMoreCards();
    }, [fetchMoreCards]);

   if (cards.length === 0) {
        return <div className="text-center p-10">Loading deck...</div>;
    }

    const card = cards[currentIndex];
    const question = card.question;
    const answer = card.answer;
    const cardId = card.id;

    return (
        <FlashCard 
            key={currentIndex}
            question={question}
            answer={answer}
            newCard={submitScore}
        />
    )
}