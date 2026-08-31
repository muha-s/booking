import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { BookingForReview } from '../../models/booking/booking-for-review';
import { HotelReviewCreate } from '../../models/hotel-review-create';
import { BookingService } from '../../services/booking';
import { HotelReviewService } from '../../services/hotel-review';

@Component({
  selector: 'app-review',
  imports: [FormsModule],
  templateUrl: './review.html',
  styleUrl: './review.css'
})
export class Review implements OnInit {

  booking = signal<BookingForReview | null>(null);

  loading = signal(true);
  sending = signal(false);
  errorMessage = signal('');
  reviewSent = signal(false);

  score: number | null = null;
  comment = '';

  readonly scores = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10];

  constructor(
    private readonly route: ActivatedRoute,
    private readonly bookingService: BookingService,
    private readonly hotelReviewService: HotelReviewService
  ) {
  }

  ngOnInit(): void {
    const bookingId = Number(this.route.snapshot.paramMap.get('bookingId'));

    if (!bookingId) {
      this.errorMessage.set('Некорректная ссылка на бронирование');
      this.loading.set(false);
      return;
    }

    this.bookingService.findMyBookingForReview(bookingId).subscribe({
      next: booking => {
        this.booking.set(booking);
        this.loading.set(false);
      },
      error: error => {
        this.loading.set(false);
        this.errorMessage.set(
          error.error?.message ?? 'Не удалось загрузить данные бронирования'
        );
      }
    });
  }

  selectScore(score: number): void {
    this.score = score;
  }

  reviewCanBeSent(): boolean {
    return this.score !== null || this.comment.trim() !== '';
  }

  submitReview(): void {
    this.errorMessage.set('');

    const booking = this.booking();

    if (booking === null) {
      return;
    }

    if (booking.status !== 'COMPLETED') {
      this.errorMessage.set('Отзыв можно оставить только после завершения проживания');
      return;
    }

    if (booking.reviewExists) {
      this.errorMessage.set('Вы уже оставили отзыв по этому бронированию');
      return;
    }

    const comment = this.comment.trim();

    if (this.score === null && comment === '') {
      this.errorMessage.set('Поставьте оценку или напишите комментарий');
      return;
    }

    if (comment.length > 200) {
      this.errorMessage.set('Комментарий не должен превышать 200 символов');
      return;
    }

    const review: HotelReviewCreate = {
      bookingId: booking.id,
      score: this.score,
      comment: comment === '' ? null : comment
    };

    this.sending.set(true);

    this.hotelReviewService.create(review).subscribe({
      next: () => {
        this.sending.set(false);
        this.reviewSent.set(true);
      },
      error: error => {
        this.sending.set(false);
        this.errorMessage.set(
          error.error?.message ?? 'Не удалось отправить отзыв'
        );
      }
    });
  }
}
